package com.example.newsworkmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val oneTimeStatusFlow = MutableStateFlow("Статус: Очікує запуску")
    private val periodicStatusFlow = MutableStateFlow("Нічна синхронізація: вимкнена")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val oneTimeStatus by oneTimeStatusFlow.collectAsState()
                    val periodicStatus by periodicStatusFlow.collectAsState()

                    NewsSyncScreen(
                        oneTimeStatus = oneTimeStatus,
                        periodicStatus = periodicStatus,
                        onSyncNowClick = { startOneTimeSync() },
                        onEnableNightSyncClick = { startPeriodicNightSync() }
                    )
                }
            }
        }
    }

    private fun startOneTimeSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString("user_token", "abc123")
            .build()

        val request = OneTimeWorkRequestBuilder<NewsSyncWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(request)

        workManager.getWorkInfoByIdLiveData(request.id)
            .observe(this) { workInfo ->
                if (workInfo != null) {
                    val status = workInfo.state.name
                    when (workInfo.state) {
                        WorkInfo.State.ENQUEUED -> {
                            oneTimeStatusFlow.value = "Статус: $status (Очікує мережу...)"
                        }
                        WorkInfo.State.RUNNING -> {
                            oneTimeStatusFlow.value = "Статус: $status (Виконується...)"
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            val message = workInfo.outputData.getString("result") ?: "Готово"
                            oneTimeStatusFlow.value = "Статус: $status — $message"
                        }
                        WorkInfo.State.FAILED -> {
                            oneTimeStatusFlow.value = "Статус: $status (Помилка)"
                        }
                        else -> {
                            oneTimeStatusFlow.value = "Статус: $status"
                        }
                    }
                }
            }
    }

    private fun startPeriodicNightSync() {
        val nightConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .build()

        val inputData = Data.Builder()
            .putString("user_token", "abc123")
            .build()

        val periodicRequest = PeriodicWorkRequestBuilder<NewsSyncWorker>(
            15,
            TimeUnit.MINUTES
        )
            .setConstraints(nightConstraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "night_news_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )

        periodicStatusFlow.value =
            "Нічна синхронізація: увімкнена (Wi-Fi + Зарядка, кожні 15 хв)"
    }
}

@Composable
fun NewsSyncScreen(
    oneTimeStatus: String,
    periodicStatus: String,
    onSyncNowClick: () -> Unit,
    onEnableNightSyncClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Синхронізація новин",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = oneTimeStatus,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSyncNowClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Синхронізувати зараз")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onEnableNightSyncClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Увімкнути нічну синхронізацію")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = periodicStatus,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}