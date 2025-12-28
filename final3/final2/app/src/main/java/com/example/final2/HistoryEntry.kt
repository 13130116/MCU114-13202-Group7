package com.example.final2

import androidx.room.Entity
import androidx.room.PrimaryKey

// 資料庫的資料結構，用來儲存歷史紀錄的資料
@Entity(tableName = "input_history")
data class HistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 自動產生的編號
    val content: String, // 內容
    val timestamp: Long  // 時間
)
