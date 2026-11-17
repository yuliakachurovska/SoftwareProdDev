package com.example.firstapplication

import android.app.Application
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Ініціалізуємо Timber тільки для Debug-збірки
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("MyApp успішно стартував!")
    }
}