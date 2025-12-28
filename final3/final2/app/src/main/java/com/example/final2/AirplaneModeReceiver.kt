package com.example.final2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 檢查廣播的內容是不是關於「飛航模式」的狀態改變
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            // 檢查飛航模式現在是「開啟」還是「關閉」
            val isAirplaneModeOn = intent.getBooleanExtra("state", false)
            if (isAirplaneModeOn) {
                // 如果飛航模式被打開了，跳出提示
                Toast.makeText(context, "✈️ 心情瓶已進入離線模式，依然可以記錄心情喔！", Toast.LENGTH_LONG).show()
            }
        }
    }
}
    