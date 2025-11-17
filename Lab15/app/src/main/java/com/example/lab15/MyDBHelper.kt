package com.example.lab15

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// 將 myTable 的欄位改為 brand, year, price
class MyDBHelper(
    context: Context,
    name: String = database,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = v
) : SQLiteOpenHelper(context, name, factory, version) {
    companion object {
        private const val database = "myCarDatabase" // 資料庫名稱
        private const val v = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 建立 myCarTable 資料表，包含廠牌(brand)、年份(year)、價格(price)
        db.execSQL(
            "CREATE TABLE myCarTable(brand TEXT PRIMARY KEY, year INTEGER NOT NULL, price INTEGER NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 更新資料庫版本時，刪除舊資料表，重新建立
        db.execSQL("DROP TABLE IF EXISTS myCarTable")
        onCreate(db)
    }
}
