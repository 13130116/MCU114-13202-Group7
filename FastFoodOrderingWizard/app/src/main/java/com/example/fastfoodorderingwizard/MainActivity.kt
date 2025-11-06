package com.example.fastfoodorderingwizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mainMeal: String? = null
    private var sideDishes: ArrayList<String>? = null
    private var drink: String? = null

    companion object {
        private const val REQUEST_MAIN_MEAL = 1
        private const val REQUEST_SIDE_DISH = 2
        private const val REQUEST_DRINK = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvMainMeal = findViewById<TextView>(R.id.tvMainMeal)
        val tvSideDish = findViewById<TextView>(R.id.tvSideDish)
        val tvDrink = findViewById<TextView>(R.id.tvDrink)

        findViewById<Button>(R.id.btnSelectMainMeal).setOnClickListener {
            val intent = Intent(this, MainMealActivity::class.java)
            startActivityForResult(intent, REQUEST_MAIN_MEAL)
        }

        findViewById<Button>(R.id.btnSelectSideDish).setOnClickListener {
            val intent = Intent(this, SideDishesActivity::class.java)
            startActivityForResult(intent, REQUEST_SIDE_DISH)
        }

        findViewById<Button>(R.id.btnSelectDrink).setOnClickListener {
            val intent = Intent(this, DrinkActivity::class.java)
            startActivityForResult(intent, REQUEST_DRINK)
        }

        findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            if (mainMeal.isNullOrEmpty() || sideDishes.isNullOrEmpty() || drink.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "Please select main meal, side dish, and drink.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val intent = Intent(this, ConfirmActivity::class.java).apply {
                    putExtra("MainMeal", mainMeal)
                    putStringArrayListExtra("sideDishes", sideDishes)
                    putExtra("drink", drink)
                }
                startActivity(intent)
            }
        }
    }

    // 處理子畫面回傳的資料
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val tvMainMeal = findViewById<TextView>(R.id.tvMainMeal)
            val tvSideDish = findViewById<TextView>(R.id.tvSideDish)
            val tvDrink = findViewById<TextView>(R.id.tvDrink)

            when (requestCode) {
                REQUEST_MAIN_MEAL -> {
                    mainMeal = data.getStringExtra("MainMeal")
                    findViewById<TextView>(R.id.tvMainMeal).text = mainMeal ?: "-"
                }

                REQUEST_SIDE_DISH -> {
                    sideDishes = data.getStringArrayListExtra("sideDishes")
                    val sideDishText = sideDishes?.joinToString(", ") ?: "-"
                    findViewById<TextView>(R.id.tvSideDish).text = sideDishText
                }


                REQUEST_DRINK -> {
                    drink = data.getStringExtra("Drink")
                    findViewById<TextView>(R.id.tvDrink).text = drink ?: "-"
                }
            }
        }
    }
}
