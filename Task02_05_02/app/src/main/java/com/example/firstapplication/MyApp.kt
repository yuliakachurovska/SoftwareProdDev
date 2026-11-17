package com.example.firstapplication

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Підписуємо детектор на життєвий цикл усього процесу застосунку
        ProcessLifecycleOwner.get().lifecycle.addObserver(BackgroundDetector())
    }
}