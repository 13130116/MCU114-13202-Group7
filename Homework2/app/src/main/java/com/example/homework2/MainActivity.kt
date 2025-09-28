package com.example.homework2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    //宣告元件變數
    private lateinit var txtShow: TextView
    private lateinit var btnZero: Button
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button
    private lateinit var btnFour: Button
    private lateinit var btnFive: Button
    private lateinit var btnSix: Button
    private lateinit var btnSeven: Button
    private lateinit var btnEight: Button
    private lateinit var btnNine: Button
    private lateinit var btnClear: Button
    private lateinit var  btnStar: Button
    private lateinit var btnDial: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //取得資料類別檔中的介面元件
        txtShow = findViewById(R.id.txtShow)
        btnZero = findViewById(R.id.btnZero)
        btnOne = findViewById(R.id.btnOne)
        btnTwo = findViewById(R.id.btnTwo)
        btnThree = findViewById(R.id.btnThree)
        btnFour = findViewById(R.id.btnFour)
        btnFive = findViewById(R.id.btnFive)
        btnSix = findViewById(R.id.btnSix)
        btnSeven = findViewById(R.id.btnSeven)
        btnEight = findViewById(R.id.btnEight)
        btnNine = findViewById(R.id.btnNine)
        btnClear = findViewById(R.id.btnClear)
        btnStar = findViewById(R.id.btnStar)
        btnDial = findViewById(R.id.btnDial)

        //設定button元件 Click事件共用 myListner
        val commonClickListener = View.OnClickListener { v ->
            val currenText  = txtShow.text.toString()
            when (v.id) {
                R.id.btnZero -> txtShow.append("0")
                R.id.btnOne -> txtShow.append("1")
                R.id.btnTwo -> txtShow.append("2")
                R.id.btnThree -> txtShow.append("3")
                R.id.btnFour -> txtShow.append("4")
                R.id.btnFive -> txtShow.append("5")
                R.id.btnSix -> txtShow.append("6")
                R.id.btnSeven -> txtShow.append("7")
                R.id.btnEight -> txtShow.append("8")
                R.id.btnNine -> txtShow.append("9")
                R.id.btnClear -> txtShow.text = "電話號碼:"
                R.id.btnStar -> txtShow.append("*")
                R.id.btnDial -> {
                    val currentPhoneNumberInView = txtShow.text.toString() // Get the absolute latest text
                    val numberToActuallyDial: String

                    if (currentPhoneNumberInView.startsWith("電話號碼:")) {
                        numberToActuallyDial = currentPhoneNumberInView.substring("電話號碼:".length)
                    } else {
                        numberToActuallyDial = currentPhoneNumberInView
                    }

                    if (numberToActuallyDial.isNotEmpty()) {
                        Log.d("DIALER", "Attempting to dial: '$numberToActuallyDial'") // Logging
                        val uri = Uri.parse("tel:$numberToActuallyDial")
                        val intent = Intent(Intent.ACTION_DIAL, uri)
                        try {
                            startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("DIALER", "Error starting dial activity", e)
                            // Maybe show a Toast to the user that dialing failed
                        }
                    } else {
                        Log.w("DIALER", "Phone number is empty, not dialing.")
                        // Maybe show a Toast to the user
                    }
                }
            }
        }
        //套用listener
        btnZero.setOnClickListener(commonClickListener)
        btnOne.setOnClickListener(commonClickListener)
        btnTwo.setOnClickListener(commonClickListener)
        btnThree.setOnClickListener(commonClickListener)
        btnFour.setOnClickListener(commonClickListener)
        btnFive.setOnClickListener(commonClickListener)
        btnSix.setOnClickListener(commonClickListener)
        btnSeven.setOnClickListener(commonClickListener)
        btnEight.setOnClickListener(commonClickListener)
        btnNine.setOnClickListener(commonClickListener)
        btnClear.setOnClickListener(commonClickListener)
        btnStar.setOnClickListener(commonClickListener)
        btnDial.setOnClickListener(commonClickListener)
    }
}