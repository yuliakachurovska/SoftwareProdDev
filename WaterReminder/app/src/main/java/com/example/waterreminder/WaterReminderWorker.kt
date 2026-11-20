package com.example.waterreminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WaterReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val waterPreferences = WaterPreferences(applicationContext)
            waterPreferences.incrementReminderCount()

            showWaterNotification()

            Log.d("WaterReminderWorker", "Нагадування про воду успішно відправлено!")
            Result.success()
        } catch (e: Exception) {
            Log.e("WaterReminderWorker", "Помилка під час виконання воркера", e)
            Result.retry()
        }
    }

    private fun showWaterNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                Log.w("WaterReminderWorker", "Немає дозволу POST_NOTIFICATIONS — сповіщення пропущено")
                return
            }
        }

        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Нагадування про воду",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Регулярні нагадування випити склянку води"
            }
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Трекер води")
            .setContentText("Час випити склянку води! 💧")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        const val CHANNEL_ID = "water_channel"
    }
}