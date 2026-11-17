package com.example.firstapplication

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            private fun logLifecycleEvent(eventName: String, activity: Activity) {
                val state = (activity as? LifecycleOwner)?.lifecycle?.currentState
                Timber.d("MyApp [$eventName] -> Екран: ${activity.javaClass.simpleName} | Поточний стан (currentState): $state")
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                logLifecycleEvent("onActivityCreated", activity)
            }

            override fun onActivityStarted(activity: Activity) {
                logLifecycleEvent("onActivityStarted", activity)
            }

            override fun onActivityResumed(activity: Activity) {
                logLifecycleEvent("onActivityResumed", activity)
            }

            override fun onActivityPaused(activity: Activity) {
                logLifecycleEvent("onActivityPaused", activity)
            }

            override fun onActivityStopped(activity: Activity) {
                logLifecycleEvent("onActivityStopped", activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                logLifecycleEvent("onActivityDestroyed", activity)
            }
        })
    }
}