package com.example.final2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MoodDao {
    @Insert
    fun insert(mood: Mood)

    @Query("SELECT * FROM moods ORDER BY id DESC")
    fun getAllMoods(): List<Mood>
}
