package com.example.final2

import androidx.room.Entity
import androidx.room.PrimaryKey

// 這張就是您的「歷史紀錄表格」
@Entity(tableName = "input_history")
data class HistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 自動產生的編號
    val content: String, // 內容
    val timestamp: Long  // 時間
)
