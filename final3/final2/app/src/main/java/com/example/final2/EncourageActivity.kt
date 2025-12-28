package com.example.final2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class EncourageActivity : AppCompatActivity() {

    private var pastMood: Mood? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encourage)

        val pastMoodCard: LinearLayout = findViewById(R.id.past_mood_card)
        val pastMoodDate: TextView = findViewById(R.id.textView_past_mood_date)
        val pastMoodContent: TextView = findViewById(R.id.textView_past_mood_content)
        val encouragementEditText: EditText = findViewById(R.id.editText_encouragement)
        val sendButton: Button = findViewById(R.id.button_send_encouragement)

        // 1. 頁面一打開，就去資料庫抽一則過去的心情
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val allMoods = db.moodDao().getAllMoods()

            if (allMoods.isEmpty()) {
                // 如果瓶子是空的，就提示使用者並關閉頁面
                Toast.makeText(this@EncourageActivity, "瓶子裡還沒有回憶喔！先去紀錄一點回憶吧~", Toast.LENGTH_LONG).show()
                finish() // 關閉這個頁面
            } else {
                // 隨機抽出一則，並顯示在畫面上
                pastMood = allMoods.random()
                pastMood?.let {
                    pastMoodCard.visibility = View.VISIBLE
                    pastMoodDate.text = "回憶日期: ${it.date}"
                    pastMoodContent.text = it.content
                }
            }
        }

        // 2. 設定「送出鼓勵」按鈕的點擊事件
        sendButton.setOnClickListener {
            val encouragementText = encouragementEditText.text.toString()
            if (encouragementText.isBlank()) {
                Toast.makeText(this, "寫點話鼓勵一下自己吧！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 組合過去的心情和現在的鼓勵，用一個漂亮的提示框顯示出來
            val finalMessage = "給那一天的你：\n\"${pastMood?.content}\"\n\n現在的我想對你說：\n\"${encouragementText}\""

            Toast.makeText(this, finalMessage, Toast.LENGTH_LONG).show()
            finish() // 顯示完畢後，關閉頁面回到主畫面
        }
    }
}
    