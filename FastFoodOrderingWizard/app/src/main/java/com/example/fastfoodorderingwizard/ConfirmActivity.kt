package com.example.fastfoodorderingwizard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        val tvConfirmMainMeal = findViewById<TextView>(R.id.tvConfirmMainMeal)
        val tvConfirmSideDishes = findViewById<TextView>(R.id.tvConfirmSideDishes)
        val tvConfirmDrink = findViewById<TextView>(R.id.tvConfirmDrink)

        // 取得傳來的資料
        val mainMeal = intent.getStringExtra("MainMeal")
        val sideDishes = intent.getStringArrayListExtra("sideDishes")
        val drink = intent.getStringExtra("drink")

        // 顯示在畫面上
        tvConfirmMainMeal.text = mainMeal ?: "-"
        tvConfirmSideDishes.text = sideDishes?.joinToString(", ") ?: "-"
        tvConfirmDrink.text = drink ?: "-"
    }
}
