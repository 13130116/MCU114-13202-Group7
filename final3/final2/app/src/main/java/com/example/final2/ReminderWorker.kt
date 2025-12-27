package com.example.final2

import android.content.Context
import android.app.NotificationManager
import androidx.work.Worker
import androidx.work.WorkerParameters
// 改用這個 import，這是最穩定的路徑
import androidx.core.app.NotificationCompat

class ReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    private fun sendNotification() {
        // 使用完整路徑來避免 "Unresolved reference" 錯誤
        val builder = androidx.core.app.NotificationCompat.Builder(applicationContext, "mood_reminder_channel")
            .setSmallIcon(android.R.drawable.ic_menu_edit)
            .setContentTitle("記得紀錄心情喔！")
            .setContentText("今天過得怎麼樣？來寫點東西吧。")
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT) // 這裡也加上完整路徑
            .setAutoCancel(true)

        try {
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1001, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
