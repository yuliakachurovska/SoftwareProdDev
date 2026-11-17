package com.example.firstapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val audioPlayer = AudioPlayer()
    private val analyticsTracker = AnalyticsTracker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Підписуємо обидва обзервери на життєвий цикл MainActivity
        lifecycle.addObserver(audioPlayer)
        lifecycle.addObserver(analyticsTracker)

        // Кнопка для переходу на SecondActivity (знадобиться для Частини 3)
        val btnOpenSecond = findViewById<Button>(R.id.btnOpenSecond)
        btnOpenSecond.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}