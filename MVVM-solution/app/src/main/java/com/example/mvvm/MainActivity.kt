package com.example.mvvm

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<TimerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "MainActivity onCreate: $this")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Підписка на зміни залишкового часу
        viewModel.timerLiveData.observe(this) { seconds: Long ->
            if (seconds == 0L) {
                binding.tvTimer.text = "Timer is over!"
            } else {
                binding.tvTimer.text = "Залишилось: $seconds сек"
            }
        }

        // 2. Підписка на стан роботи таймера (блокуємо кнопку "Старт" під час відліку)
        viewModel.isTimerRunning.observe(this) { isRunning: Boolean ->
            binding.btnStart.isEnabled = !isRunning
        }

        // 3. Обробка натискання кнопки "Старт"
        binding.btnStart.setOnClickListener {
            val inputText = binding.etSeconds.text.toString()
            // Якщо поле порожнє, некоректне або <= 0 — беремо 30 секунд за замовчуванням
            val userSeconds = inputText.toLongOrNull()?.takeIf { it > 0L } ?: 30L
            viewModel.startTimer(userSeconds)
        }

        // 4. Обробка натискання кнопки "Скидання"
        binding.btnReset.setOnClickListener {
            viewModel.resetTimer()
        }
    }

    companion object {
        const val TAG = "XXXX"
    }
}