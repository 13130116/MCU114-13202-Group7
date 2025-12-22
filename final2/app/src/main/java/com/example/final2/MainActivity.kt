package com.example.final2

// 🧹 新增：把動畫需要用到的工具都先請進來
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView // 這是圖片工具
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    // 🧹 把我們設計圖上所有的道具都宣告出來
    private lateinit var editTextMood: EditText
    private lateinit var saveButton: Button
    private lateinit var historyButton: Button
    private lateinit var bottleImage: ImageView
    private lateinit var sparkleImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // 我們直接用這行設定設計圖就好

        // 把櫃子準備好
        db = AppDatabase.getDatabase(this)

        // 找到設計圖上所有的道具，讓程式認識它們
        editTextMood = findViewById(R.id.editText_mood)
        saveButton = findViewById(R.id.button_save_mood)
        historyButton = findViewById(R.id.button_view_history)
        bottleImage = findViewById(R.id.imageView_bottle)
        sparkleImage = findViewById(R.id.imageView_sparkle)

        // 設定「裝進情緒瓶」按鈕按下去要做的事
        saveButton.setOnClickListener {
            val moodText = editTextMood.text.toString()
            if (moodText.isNotBlank()) {
                // 🧹 按鈕按下去後，不是馬上儲存，而是先去播放動畫！
                playBottlingAnimation(moodText)
            } else {
                Toast.makeText(this, "請先輸入你的心情！", Toast.LENGTH_SHORT).show()
            }
        }

        // 設定「查看過去的瓶子」按鈕
        historyButton.setOnClickListener {
            val intent = Intent(this, MoodHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 這就是我們的「裝瓶動畫」魔術劇本
     */
    private fun playBottlingAnimation(moodText: String) {
        // 魔法開始前，先讓儲存按鈕暫時不能按，才不會一直重複點
        saveButton.isEnabled = false

        // 1. 讓「心情光點」出現在輸入框的上方中央
        sparkleImage.x = editTextMood.x + (editTextMood.width / 2) - (sparkleImage.width / 2)
        sparkleImage.y = editTextMood.y
        sparkleImage.visibility = View.VISIBLE // 讓隱藏的光點現身

        // 2. 變魔術！讓光點「飛」進瓶子裡
        // 我們計算出瓶子的中心點當作飛行的目的地
        val targetY = bottleImage.y + (bottleImage.height / 2) - (sparkleImage.height / 2)

        // 這是動畫的核心指令
        val animator = ObjectAnimator.ofFloat(sparkleImage, "translationY", sparkleImage.y, targetY).apply {
            duration = 1200 // 動畫持續 1.2 秒

            // 3. 我們告訴程式，動畫一結束，就要做以下這些事
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // a. 讓光點飛進瓶子後消失
                    sparkleImage.visibility = View.INVISIBLE
                    // b. 把心情偷偷存到櫃子裡
                    saveMoodToDatabase(moodText)
                    // c. 跳出成功訊息
                    Toast.makeText(this@MainActivity, "心情已裝進瓶中！", Toast.LENGTH_SHORT).show()
                    // d. 把輸入框清空
                    editTextMood.text.clear()
                    // e. 最後，讓儲存按鈕可以重新被點擊
                    saveButton.isEnabled = true
                }
            })
        }

        // 魔術開始！
        animator.start()
    }

    /**
     * 這是一個專門負責把心情存到櫃子裡的小幫手函式
     */
    private fun saveMoodToDatabase(moodText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val entry = HistoryEntry(content = moodText)
            db.historyDao().insert(entry)
        }
    }
}