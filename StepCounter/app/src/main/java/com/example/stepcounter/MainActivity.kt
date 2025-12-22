package com.example.stepcounter // 請確保這與您的套件名稱相符

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stepcounter.ui.theme.StepCounterTheme

// --- ViewModel 定義 ---
// 將 ViewModel 的程式碼直接放在這裡，方便管理



// --- Activity 主體 ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StepCounterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFDEEF4)
                ) {
                    SimpleCounterScreen()
                }
            }
        }
    }
}

// --- Composable UI 介面 ---
@Composable
fun SimpleCounterScreen(counterViewModel: CounterViewModel = viewModel()) {
    // 從 ViewModel 獲取 count 的值
    val count by counterViewModel.count

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // "計數器" 標題
        Text(
            text = "計數器",
            style = TextStyle(fontSize = 28.sp, color = Color.DarkGray, textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 顯示計數的數字
        Text(
            text = "$count",
            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray, textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // "點我加一" 按鈕
        Button(
            // 點擊時，呼叫 ViewModel 中的 increment 函數
            onClick = { counterViewModel.increment() },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp)
        ) {
            Text(text = "點我加一", fontSize = 18.sp)
        }
    }
}

// --- 預覽 ---
@Preview(showBackground = true, name = "Simple Counter Preview")
@Composable
fun DefaultPreview() {
    StepCounterTheme {
        Surface(color = Color(0xFFFDEEF4)) {
            SimpleCounterScreen()
        }
    }
}
