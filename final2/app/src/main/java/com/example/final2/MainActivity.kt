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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextMood: EditText = findViewById(R.id.editText_mood)
        val saveButton: Button = findViewById(R.id.button_save_mood)
        val historyButton: Button = findViewById(R.id.button_view_history)

        saveButton.setOnClickListener {
            val moodText = editTextMood.text.toString()
            if (moodText.isNotBlank()) {
                Toast.makeText(this, "心情已儲存：$moodText", Toast.LENGTH_SHORT).show()
                editTextMood.text.clear()
            } else {
                Toast.makeText(this, "請先輸入你的心情！", Toast.LENGTH_SHORT).show()
            }
        }


        historyButton.setOnClickListener {

            val intent = Intent(this, MoodHistoryActivity::class.java)


            startActivity(intent)
        }
    }
}
