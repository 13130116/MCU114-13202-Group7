package com.example.final2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


import com.example.final2.AppDatabase
import com.example.final2.Mood
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoodHistoryActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_history)

        // 初始化資料庫
        db = AppDatabase.getDatabase(this)

        val historyTextView: TextView = findViewById(R.id.history_text_view)

        // 呼叫顯示歷史紀錄的函式
        loadAndShowHistory(historyTextView)

        // 設定返回按鈕
        val backButton: Button = findViewById(R.id.button_back)
        backButton.setOnClickListener {
            finish() // 關閉目前頁面，返回上一頁
        }
    }

    private fun loadAndShowHistory(textView: TextView) {
        // 使用 Coroutine 在背景執行緒讀取資料庫，避免卡住畫面
        CoroutineScope(Dispatchers.IO).launch {

            // 從資料庫讀取所有心情
            // 如果 getAllMoods() 還是紅字，請確認您的 MoodDao.kt 是否有改好 (回傳 List<Mood>)
            val moodList = db.moodDao().getAllMoods()

            val historyText = StringBuilder()

            if (moodList.isEmpty()) {
                historyText.append("目前沒有任何心情紀錄喔！快去首頁新增吧。")
            } else {
                // 遍歷每一筆心情資料
                moodList.forEach { mood ->
                    // 這裡把日期和心情內容串接起來
                    historyText.append("📅 ${mood.date}\n")
                    historyText.append("📝 ${mood.content}\n")
                    historyText.append("-----------------\n\n")
                }
            }

            // 切換回主執行緒 (Main Thread) 更新 UI
            withContext(Dispatchers.Main) {
                textView.text = historyText.toString()
            }
        }
    }
}
