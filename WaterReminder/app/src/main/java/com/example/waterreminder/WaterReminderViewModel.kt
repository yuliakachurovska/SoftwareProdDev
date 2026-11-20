package com.example.waterreminder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit

class WaterReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val workManager = WorkManager.getInstance(context)
    private val waterPreferences = WaterPreferences(context)

    val reminderCount: StateFlow<Int> = waterPreferences.reminderCountFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val isReminderActive: StateFlow<Boolean> = workManager
        .getWorkInfosForUniqueWorkFlow(UNIQUE_WORK_NAME)
        .map { workInfos ->
            workInfos.any { !it.state.isFinished }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun startReminder(intervalMinutes: Long) {
        val safeInterval = if (intervalMinutes < 15) 15L else intervalMinutes

        val request = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            safeInterval,
            TimeUnit.MINUTES
        ).build()

        workManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request
        )
    }

    fun stopReminder() {
        workManager.cancelUniqueWork(UNIQUE_WORK_NAME)
    }

    companion object {
        const val UNIQUE_WORK_NAME = "water_reminder"
    }
}