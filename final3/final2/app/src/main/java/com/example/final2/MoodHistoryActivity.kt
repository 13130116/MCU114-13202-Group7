package com.example.final2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // ★★★ 這裡我們改用一個比較新的、更安全的方法
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MoodHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_history)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_history)
        val backButton: Button = findViewById(R.id.button_back)

        // 設定 RecyclerView 的排列方式 (垂直排列)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 讀取資料並顯示
        loadAndShowHistory(recyclerView)

        backButton.setOnClickListener {
            finish() // 關閉這個頁面，返回主畫面
        }
    }

    private fun loadAndShowHistory(recyclerView: RecyclerView) {
        // 使用 lifecycleScope 可以更安全地在背景執行緒讀取資料庫
        // 它會跟著這個畫面的生命週期，畫面關閉時，它也會自動停止，比較不會出錯
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            // .reversed() 可以讓最新的紀錄顯示在最上面
            val moodList = db.moodDao().getAllMoods().reversed()

            // 把讀取到的資料交給 MoodAdapter，並讓 RecyclerView 顯示出來
            recyclerView.adapter = MoodAdapter(this@MoodHistoryActivity, moodList)
        }
    }
}
