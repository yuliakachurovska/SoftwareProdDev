package com.example.mvvm

import android.os.CountDownTimer
import android.util.Log
import com.example.mvvm.MainActivity.Companion.TAG

class TimerModel {

    private var countDownTimer: CountDownTimer? = null

    fun start(
        durationSeconds: Long = 30L,
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ) {
        stop()

        Log.d(TAG, "TimerModel: старт відліку на $durationSeconds сек")
        onTick(durationSeconds)

        countDownTimer = object : CountDownTimer(durationSeconds * 1_000L, 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1_000L
                Log.d(TAG, "TimerModel onTick: залишилось $secondsLeft сек")
                onTick(secondsLeft)
            }

            override fun onFinish() {
                Log.d(TAG, "TimerModel onFinish: таймер завершено!")
                countDownTimer = null
                onFinish()
            }
        }.start()
    }

    fun stop() {
        if (countDownTimer != null) {
            Log.d(TAG, "TimerModel: зупинка таймера")
            countDownTimer?.cancel()
            countDownTimer = null
        }
    }
}