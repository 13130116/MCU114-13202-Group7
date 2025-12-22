// AppDatabase.kt

package com.example.final2

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. 告訴手機，「抽屜」裡要放什麼東西
@Entity(tableName = "input_history") // 「抽屜」的名字叫 input_history
data class HistoryEntry(
    @PrimaryKey(autoGenerate = true) // 每一筆紀錄都有一個自動增加的編號
    val id: Int = 0,

    val content: String, // 這是要存放使用者輸入內容的地方

    val timestamp: Long = System.currentTimeMillis() // 紀錄存進來的時間
)

// 2. 告訴手機，我們的「櫃子」長什麼樣
@Database(entities = [HistoryEntry::class], version = 1) // 櫃子裡有 HistoryEntry 這種抽屜
abstract class AppDatabase : RoomDatabase() {
    // 這個是拿來操作「抽屜」的工具
    abstract fun historyDao(): HistoryDao

    // 這是讓整個 App 共用同一個「櫃子」的方法，不用管細節
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // 「櫃子」的檔案名稱
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
