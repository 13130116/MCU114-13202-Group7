package com.example.final2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
// 注意：這裡不需要 import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert
    fun insert(mood: Mood)

    // ★★★ 關鍵修改：拿掉 Flow，直接回傳 List<Mood> ★★★
    @Query("SELECT * FROM mood_table ORDER BY id DESC")
    fun getAllMoods(): List<Mood>
}
