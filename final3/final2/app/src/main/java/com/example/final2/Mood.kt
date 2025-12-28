package com.example.final2

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "moods")
data class Mood(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val content: String,
    val moodIcon: String? = null
)

