package com.example.final2

import android.os.Bundle
import android.widget.Button // 🧹 新增：把按鈕工具拿進來
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoodHistoryActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_history)

        // --- 我們從這裡開始修改 ---

        db = AppDatabase.getDatabase(this)

        val historyTextView: TextView = findViewById(R.id.history_text_view)
        loadAndShowHistory(historyTextView)

        // 🧹 1. 在程式裡找到我們剛剛畫好的按鈕
        val backButton: Button = findViewById(R.id.button_back)

        // 🧹 2. 告訴程式，這個按鈕按下去要做什麼事
        backButton.setOnClickListener {
            finish() // finish() 這個指令的意思就是「關閉目前這個頁面，返回上一頁」
        }
    }

    private fun loadAndShowHistory(textView: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            val historyList = db.historyDao().getAll()

            val historyText = StringBuilder()
            if (historyList.isEmpty()) {
                // 🧹 如果沒有任何紀錄，就顯示提示訊息
                historyText.append("目前沒有任何心情紀錄喔！")
            } else {
                historyList.forEach { entry ->
                    historyText.append("- ${entry.content}\n\n")
                }
            }

            withContext(Dispatchers.Main) {
                textView.text = historyText.toString()
            }
        }
    }
}
