
package com.example.final2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

// ↓↓↓ 如果 Mood 和 AppDatabase 還是紅字，請確保這兩個檔案真的存在，或者手動解開下面這兩行的註解 ↓↓↓
 import com.example.final2.Mood
 import com.example.final2.AppDatabase

class MainActivity : AppCompatActivity() {

    // --- 變數宣告 ---
    private var startTime: Long = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var usageTimeText: TextView

    // 勵志語錄列表
    private val quotes = listOf(
        "擁抱你的每一種情緒，它們都是你的一部分。",
        "今天的你已經做得很好了，休息一下吧。",
        "即使是烏雲密布的日子，雲層之上依然有陽光。",
        "深呼吸，這一切都會過去的。",
        "接受自己的不完美，那是你獨特的光芒。",
        "每一次的低潮，都是為了下一次的跳躍蓄力。",
        "別忘了對自己溫柔一點。",
        "你的感受是真實的，而且很重要。",
        "慢慢來，比較快。",
        "生活不一定要完美才值得慶祝。"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 初始化儲存工具
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        createNotificationChannel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextMood: EditText = findViewById(R.id.editText_mood)
        val saveButton: Button = findViewById(R.id.button_save_mood)
        val historyButton: Button = findViewById(R.id.button_view_history)

        // --- 提醒開關設定 ---
        val reminderSwitch: Switch = findViewById(R.id.switch_reminder)
        val isReminderEnabled = sharedPreferences.getBoolean("reminder_enabled", false)
        reminderSwitch.isChecked = isReminderEnabled

        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
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

        // --- 顯示上次停留時間 ---
        usageTimeText = findViewById(R.id.textView_usage_time)
        val lastDuration = sharedPreferences.getLong("last_duration", 0)
        if (lastDuration > 0) {
            usageTimeText.text = "上次停留時間：$lastDuration 秒"
        } else {
            usageTimeText.text = "歡迎首次使用！"
        }

        // --- 按鈕事件：儲存心情 ---
        saveButton.setOnClickListener {
            val moodText = editTextMood.text.toString()
            if (moodText.isNotBlank()) {

                // 1. 取得現在時間
                val currentDate = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date())

                // 2. 建立 Mood 資料物件
                // 如果 Mood 是紅字，請按 Alt+Enter 匯入
                val mood = Mood(date = currentDate, content = moodText)

                // 3. 【關鍵】開啟背景執行緒，將資料寫入資料庫
                Thread {
                    // 如果 AppDatabase 是紅字，請按 Alt+Enter 匯入
                    val db = AppDatabase.getDatabase(this)
                    db.moodDao().insert(mood)
                }.start()

                // 4. 隨機抽取一句話並顯示
                val randomQuote = quotes.random()
                Toast.makeText(this, "心情已裝進瓶子！\n\n💡 $randomQuote", Toast.LENGTH_LONG).show()

                // 5. 清空輸入框
                editTextMood.text.clear()
            } else {
                Toast.makeText(this, "請先輸入你的心情！", Toast.LENGTH_SHORT).show()
            }
        }

        historyButton.setOnClickListener {
            val intent = Intent(this, MoodHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    // App 開啟時記錄時間
    override fun onResume() {
        super.onResume()
        startTime = System.currentTimeMillis()
    }

    // App 離開/暫停時計算並儲存時間
    override fun onPause() {
        super.onPause()
        val endTime = System.currentTimeMillis()
        val duration = (endTime - startTime) / 1000

        sharedPreferences.edit().putLong("last_duration", duration).apply()
        Log.d("UsageTracker", "這次停留了: $duration 秒")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "每日心情提醒"
            val descriptionText = "提醒你紀錄今天的心情"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("mood_reminder_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
