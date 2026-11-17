package com.example.firstapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        тут то і буде виток
        UserTracker.register(this)

        Timber.d("Зареєстровано слухачів у UserTracker: ${UserTracker.getListenersCount()}")
    }
}