package com.example.fastfoodorderingwizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DrinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupDrinks)
        val btnConfirm = findViewById<Button>(R.id.btnConfirmDrink)

        btnConfirm.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(this, "Please select a drink", Toast.LENGTH_SHORT).show()
            } else {
                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                val selectedDrink = selectedRadioButton.text.toString()

                val resultIntent = Intent()
                resultIntent.putExtra("Drink", selectedDrink)
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // 關閉 DrinkActivity，回到 MainActivity
            }
        }
    }
}
