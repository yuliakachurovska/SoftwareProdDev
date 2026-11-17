package com.example.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm.MainActivity.Companion.TAG

class TimerViewModel : ViewModel() {

    private val timerModel = TimerModel()

    // Інкапсуляція LiveData для часу (за замовчуванням 30 секунд)
    private val _timerLiveData = MutableLiveData<Long>(30L)
    val timerLiveData: LiveData<Long>
        get() = _timerLiveData

    private val _isTimerRunning = MutableLiveData<Boolean>(false)
    val isTimerRunning: LiveData<Boolean>
        get() = _isTimerRunning

    init {
        Log.d(TAG, "TimerViewModel created")
    }

    fun startTimer(userSeconds: Long = 30L) {
        _isTimerRunning.value = true

        timerModel.start(
            durationSeconds = userSeconds,
            onTick = { secondsLeft ->
                _timerLiveData.value = secondsLeft
            },
            onFinish = {
                _timerLiveData.value = 0L
                _isTimerRunning.value = false
            }
        )
    }

    // зупинка таймера та повернення до початкових 30 секунд
    fun resetTimer() {
        Log.d(TAG, "TimerViewModel: скидання таймера до 30 сек")
        timerModel.stop()
        _timerLiveData.value = 30L
        _isTimerRunning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "TimerViewModel onCleared: остаточне знищення, зупиняємо таймер")
        timerModel.stop()
    }
}