package com.example.firstapplication

import android.content.Context
object UserTracker {
    private val listeners = mutableListOf<Context>()

    fun register(context: Context) {
//        listeners.add(context) для витоку пам'яті
        listeners.add(context.applicationContext)
    }

    fun getListenersCount(): Int = listeners.size
}