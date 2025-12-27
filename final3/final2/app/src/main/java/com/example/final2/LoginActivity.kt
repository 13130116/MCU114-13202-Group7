package com.example.final2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. 連結佈局檔案 (這是最關鍵的一步)
        setContentView(R.layout.activity_login)

        // 為根視圖設置 Padding 以避免與系統欄重疊
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. 透過 ID 找到畫面上的元件
        val usernameInput: EditText = findViewById(R.id.editText_username)
        val passwordInput: EditText = findViewById(R.id.editText_password)
        val loginButton: Button = findViewById(R.id.button_login)

        // 3. 設定登入按鈕的點擊監聽器
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            // 4. 簡單的登入驗證
            // 我們暫時設定，只要帳號和密碼不是空白，就當作登入成功
            if (username.isNotBlank() && password.isNotBlank()) {
                // 登入成功
                Toast.makeText(this, "登入成功！", Toast.LENGTH_SHORT).show()

                // 準備跳轉到 MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                // 結束目前的登入頁面，這樣按「返回」才不會又回到這裡
                finish()

            } else {
                // 登入失敗
                Toast.makeText(this, "帳號或密碼不能為空！", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
