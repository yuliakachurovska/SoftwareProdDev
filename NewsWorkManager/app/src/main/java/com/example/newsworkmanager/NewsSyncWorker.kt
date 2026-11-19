package com.example.newsworkmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class NewsSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val token = inputData.getString("user_token")
        Log.d("NewsSyncWorker", "Починаємо синхронізацію новин для токена: $token")

        delay(3000)

        val out = Data.Builder()
            .putString("result", "Успіх! Завантажено останні новини")
            .build()

        Log.d("NewsSyncWorker", "Синхронізацію завершено успішно!")

        return Result.success(out)
    }
}