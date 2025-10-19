package com.example.fastfoodorderingwizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SideDishesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_dishes)

        val cbFries = findViewById<CheckBox>(R.id.cbFries)
        val cbSalad = findViewById<CheckBox>(R.id.cbSalad)
        val cbSoup = findViewById<CheckBox>(R.id.cbCornCup)
        val btnConfirm = findViewById<Button>(R.id.btnConfirmSideDish)

        btnConfirm.setOnClickListener {
            val selectedSideDishes = arrayListOf<String>()

            if (cbFries.isChecked) selectedSideDishes.add(cbFries.text.toString())
            if (cbSalad.isChecked) selectedSideDishes.add(cbSalad.text.toString())
            if (cbSoup.isChecked) selectedSideDishes.add(cbSoup.text.toString())

            if (selectedSideDishes.isEmpty()) {
                Toast.makeText(this, "Please select at least one side dish", Toast.LENGTH_SHORT).show()
            } else {
                val resultIntent = Intent()
                resultIntent.putStringArrayListExtra("sideDishes", selectedSideDishes)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
