package com.example.lab15

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab15.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase

        // 宣告 Adapter 並連結 ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        binding.listView.adapter = adapter

        // 設定監聽器
        setListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close() // 關閉資料庫
    }

    private fun setListener() {
        binding.btnInsert.setOnClickListener {
            // 判斷所有欄位是否都已填入
            if (binding.edBrand.length() < 1 || binding.edYear.length() < 1 || binding.edPrice.length() < 1) {
                showToast("所有欄位請勿留空")
            } else {
                try {
                    // 新增一筆汽車紀錄於 myCarTable 資料表
                    dbrw.execSQL(
                        "INSERT INTO myCarTable(brand, year, price) VALUES(?, ?, ?)",
                        arrayOf(
                            binding.edBrand.text.toString(),
                            binding.edYear.text.toString(),
                            binding.edPrice.text.toString()
                        )
                    )
                    showToast("新增: ${binding.edBrand.text}, 年份: ${binding.edYear.text}, 價格: ${binding.edPrice.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("新增失敗: $e")
                }
            }
        }

        binding.btnUpdate.setOnClickListener {
            // 判斷廠牌和價格是否有填入
            if (binding.edBrand.length() < 1 || binding.edPrice.length() < 1) {
                showToast("廠牌和價格欄位請勿留空")
            } else {
                try {
                    // 更新相同廠牌的年份和價格
                    dbrw.execSQL(
                        "UPDATE myCarTable SET year = ?, price = ? WHERE brand LIKE ?",
                        arrayOf(
                            binding.edYear.text.toString(),
                            binding.edPrice.text.toString(),
                            binding.edBrand.text.toString()
                        )
                    )
                    showToast("更新: ${binding.edBrand.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("更新失敗: $e")
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            // 判斷是否有填入廠牌
            if (binding.edBrand.length() < 1) {
                showToast("廠牌請勿留空")
            } else {
                try {
                    // 從 myCarTable 資料表刪除相同廠牌的紀錄
                    dbrw.execSQL("DELETE FROM myCarTable WHERE brand LIKE ?", arrayOf(binding.edBrand.text.toString()))
                    showToast("刪除: ${binding.edBrand.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("刪除失敗: $e")
                }
            }
        }

        binding.btnQuery.setOnClickListener {
            // 若無輸入廠牌則查詢全部，反之查詢該廠牌資料
            val queryString = if (binding.edBrand.length() < 1) {
                "SELECT * FROM myCarTable"
            } else {
                "SELECT * FROM myCarTable WHERE brand LIKE '${binding.edBrand.text}'"
            }

            val c = dbrw.rawQuery(queryString, null)
            c.moveToFirst()
            items.clear()
            showToast("共有 ${c.count} 筆資料")
            for (i in 0 until c.count) {
                // 加入新資料，格式為 "廠牌: [廠牌], 年份: [年份], 價格: [價格]"
                items.add("廠牌: ${c.getString(0)}\n年份: ${c.getInt(1)}, 價格: ${c.getInt(2)}")
                c.moveToNext()
            }
            adapter.notifyDataSetChanged()
            c.close()
        }
    }

    private fun showToast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    private fun cleanEditText() {
        binding.edBrand.setText("")
        binding.edYear.setText("")
        binding.edPrice.setText("")
    }
}

