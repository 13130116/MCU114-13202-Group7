package com.example.final2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ↓↓↓ 1. 原本的心情 Dao
@Database(entities = [Mood::class, HistoryEntry::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 2. 原本的心情 Dao
    abstract fun moodDao(): MoodDao

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
