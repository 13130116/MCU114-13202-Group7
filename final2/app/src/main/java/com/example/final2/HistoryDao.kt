package com.example.final2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    // 說明書第一條：如何「放東西進去」
    @Insert
    suspend fun insert(entry: HistoryEntry)

    // 說明書第二條：如何「把所有東西拿出來看」，並且從最新的開始排
    @Query("SELECT * FROM input_history ORDER BY timestamp DESC")
    suspend fun getAll(): List<HistoryEntry>
}