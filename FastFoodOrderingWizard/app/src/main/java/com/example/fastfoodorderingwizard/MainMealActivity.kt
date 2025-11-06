package com.example.fastfoodorderingwizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainMealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_meal)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupMainMeals)
        val btnConfirm = findViewById<Button>(R.id.btnConfirmMainMeal)

        btnConfirm.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(this, "Please select a main meal", Toast.LENGTH_SHORT).show()
            } else {
                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                val selectedMeal = selectedRadioButton.text.toString()

                val resultIntent = Intent()
                resultIntent.putExtra("MainMeal", selectedMeal)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
