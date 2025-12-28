package com.example.final2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu // ★ 新增 import
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // ★ 新增 import
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch // ★ 新增 import
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var startTime: Long = 0//記錄使用者開啟時間
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var usageTimeText: TextView//顯示上次停留時間
    private var selectedMoodIcon: String? = null//記錄使用者選了哪個心情圖標
    private lateinit var moodIconImageViews: List<ImageView>//五個心情圖標的清單
    //心理小語的紀錄，儲存時隨機抽選
    private val quotes = listOf(
        "擁抱你的每一種情緒，它們都是你的一部分。", "今天的你已經做得很好了，休息一下吧。",
        "即使是烏雲密布的日子，雲層之上依然有陽光。", "深呼吸，這一切都會過去的。",
        "接受自己的不完美，那是你獨特的光芒。", "每一次的低潮，都是為了下一次的跳躍蓄力。",
        "別忘了對自己溫柔一點。", "你的感受是真實的，而且很重要。",
        "慢慢來，比較快。", "生活不一定要完美才值得慶祝。"
    )
    //監聽使用者是否開啟飛航模式
    private val airplaneModeReceiver = AirplaneModeReceiver() // ★★★ 新增：宣告我們的廣播接收器

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        createNotificationChannel()//發送每日提醒

        val editTextMood: EditText = findViewById(R.id.editText_mood)
        val saveButton: Button = findViewById(R.id.button_save_mood)
        val reminderSwitch: Switch = findViewById(R.id.switch_reminder)
        usageTimeText = findViewById(R.id.textView_usage_time)

        // 設定心情圖案點擊事件
        setupMoodIcons()

        // 設定提醒開關
        val isReminderEnabled = sharedPreferences.getBoolean("reminder_enabled", false)
        reminderSwitch.isChecked = isReminderEnabled
        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            //這裡在處理開關被打開還是關著的
            sharedPreferences.edit().putBoolean("reminder_enabled", isChecked).apply()
            if (isChecked) {
                val request = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS).build()
                WorkManager.getInstance(this).enqueue(request)
                Toast.makeText(this, "每日提醒已開啟", Toast.LENGTH_SHORT).show()
            } else {
                WorkManager.getInstance(this).cancelAllWork()
                Toast.makeText(this, "提醒已關閉", Toast.LENGTH_SHORT).show()
            }
        }

        // 顯示上次停留時間
        val lastDuration = sharedPreferences.getLong("last_duration", 0)
        usageTimeText.text = if (lastDuration > 0) "上次停留時間：$lastDuration 秒" else "歡迎首次使用！"

        // 儲存心情按鈕事件
        saveButton.setOnClickListener {
            saveMood(editTextMood)
        }

        // 設定「更多功能」按鈕的點擊事件，點擊後跳出選單
        val moreOptionsButton: Button = findViewById(R.id.button_more_options)
        moreOptionsButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    // 更多功能的選單
    private fun showPopupMenu(anchorView: View) {
        val popupMenu = PopupMenu(this, anchorView)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        // 設定選項點擊事件
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                // 選項一：查看過去的瓶子
                R.id.menu_view_history -> {
                    val intent = Intent(this, MoodHistoryActivity::class.java)
                    startActivity(intent)
                    true
                }
                // 選項二：提供意見回饋
                R.id.menu_feedback -> {
                    val intent = Intent(this, FeedbackActivity::class.java)
                    startActivity(intent)
                    true
                }
                // 選項三：鼓勵過去的自己
                R.id.menu_encourage -> {
                    val intent = Intent(this, EncourageActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
        //顯示選單
    }

    // ★★★ 鼓勵過去的自己 ★★★
    private fun encouragePastSelf() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val allMoods = db.moodDao().getAllMoods()

            if (allMoods.isEmpty()) {
                Toast.makeText(this@MainActivity, "瓶子裡還是空的呢，先寫下今天的心情吧！", Toast.LENGTH_LONG).show()
            } else {
                val randomMood = allMoods.random() // 隨機抽取一則心情
                val message = "記得那一天嗎？\n\n「${randomMood.content}」\n\n無論當時心情如何，你都走過來了，你很棒！"
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }
        }
    }
    //儲存心情
    private fun saveMood(editTextMood: EditText) {
        val moodText = editTextMood.text.toString()
        if (moodText.isNotBlank() && selectedMoodIcon != null) {
            val currentDate = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date())
            val mood = Mood(date = currentDate, content = moodText, moodIcon = selectedMoodIcon)

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                db.moodDao().insert(mood)
                val randomQuote = quotes.random()
                Toast.makeText(this@MainActivity, "心情已裝進瓶子！\n\n💡 $randomQuote", Toast.LENGTH_LONG).show()
                editTextMood.text.clear()
                clearMoodSelection()
            }
        } else if (moodText.isBlank()) {
            Toast.makeText(this, "請先輸入你的心情！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "請選擇一個心情圖案！", Toast.LENGTH_SHORT).show()
        }
    }

    //設定心情圖標點擊後的效果
    private fun setupMoodIcons() {
        //設定五個心情的點擊動畫
        val iconHappy: ImageView = findViewById(R.id.icon_happy)
        val iconSad: ImageView = findViewById(R.id.icon_sad)
        val iconNormal: ImageView = findViewById(R.id.icon_normal)
        val iconAngry: ImageView = findViewById(R.id.icon_angry)
        val iconShy: ImageView = findViewById(R.id.icon_shy)
        moodIconImageViews = listOf(iconHappy, iconSad, iconNormal, iconAngry, iconShy)
        val iconClickListener = View.OnClickListener { view ->
            selectedMoodIcon = resources.getResourceEntryName(view.id).removePrefix("icon_")
            moodIconImageViews.forEach { icon ->
                if (icon.id == view.id) {
                    icon.scaleX = 1.2f; icon.scaleY = 1.2f; icon.alpha = 1.0f
                } else {
                    icon.scaleX = 0.9f; icon.scaleY = 0.9f; icon.alpha = 0.6f
                }
            }
        }
        moodIconImageViews.forEach { it.setOnClickListener(iconClickListener) }
    }
    //清除心情圖標選擇
    private fun clearMoodSelection() {
        selectedMoodIcon = null
        moodIconImageViews.forEach { icon ->
            icon.scaleX = 1.0f; icon.scaleY = 1.0f; icon.alpha = 1.0f
        }
    }

    override fun onResume() {
        super.onResume()

        startTime = System.currentTimeMillis()//紀錄開始的時間
        clearMoodSelection()//清除上次選擇，讓使用者可以重新選擇
        //開啟監聽飛航模式
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, intentFilter)

    }

    override fun onPause() {
        super.onPause()
        //計算使用者停留時間
        if (startTime > 0) {
            val endTime = System.currentTimeMillis()
            val duration = (endTime - startTime) / 1000
            sharedPreferences.edit().putLong("last_duration", duration).apply()
            Log.d("UsageTracker", "這次停留了: $duration 秒")
        }
        //關閉監聽
        unregisterReceiver(airplaneModeReceiver)
    }
    //通知的設定
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "每日心情提醒"
            val descriptionText = "提醒你紀錄今天的心情"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("mood_reminder_channel", name, importance).apply { description = descriptionText }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
