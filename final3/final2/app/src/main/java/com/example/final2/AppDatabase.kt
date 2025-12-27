package com.example.final2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ↓↓↓ 1. 這裡要把 Mood 和 HistoryEntry 都加進來 (用逗號隔開) ↓↓↓
@Database(entities = [Mood::class, HistoryEntry::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 2. 原本的心情 Dao
    abstract fun moodDao(): MoodDao

    // ↓↓↓ 3. 補上這行！這樣 MoodHistoryActivity 就不會報錯了 ↓↓↓
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // 建議統一改個名字，例如 app_database
                )
                    // ↓↓↓ 4.這行很重要！因為我們改了資料庫結構，加上這個避免閃退 ↓↓↓
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
