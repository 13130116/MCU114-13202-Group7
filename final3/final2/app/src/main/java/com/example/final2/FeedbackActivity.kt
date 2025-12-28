package com.example.final2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val editTextFeedback: EditText = findViewById(R.id.editText_feedback_content)
        val sendButton: Button = findViewById(R.id.button_send_feedback)

        sendButton.setOnClickListener {
            val feedbackText = editTextFeedback.text.toString()

            if (feedbackText.isNotBlank()) {
                // 呼叫手機的 Email App
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    // 告訴手機要寄信
                    data = Uri.parse("mailto:")

                    // ★★★ 把這裡的信箱換成您自己的 Email 地址 ★★★
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("your_email@example.com"))

                    // 設定信件標題
                    putExtra(Intent.EXTRA_SUBJECT, "情緒瓶 App 使用者回饋")

                    // 把使用者寫的內容放進信件內文
                    putExtra(Intent.EXTRA_TEXT, feedbackText)
                }

                // 檢查手機裡有沒有可以寄信的 App
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                    Toast.makeText(this, "謝謝您寶貴的意見!我們會繼續努力", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(this, "您的手機上沒有安裝電子郵件 App", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "請先輸入您的意見！", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
    //這邊是AI協助