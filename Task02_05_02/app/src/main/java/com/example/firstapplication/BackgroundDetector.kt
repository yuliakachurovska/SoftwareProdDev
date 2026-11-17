package com.example.firstapplication

// це для першого етапу завдання було
//import timber.log.Timber
//
//class BackgroundDetector {
//    private var activeActivitiesCount = 0
//
//    fun onActivityStarted() {
//        activeActivitiesCount++
//        if (activeActivitiesCount == 1) {
//            Timber.d("Застосунок тепер ВИДИМИЙ на екрані (Foreground)")
//        }
//    }
//
//    fun onActivityStopped() {
//        activeActivitiesCount--
//        if (activeActivitiesCount == 0) {
//            Timber.d("Застосунок тепер у ФОНІ (Background)")
//        }
//    }
//}

//це другий етап
//import android.app.Activity
//import android.app.Application
//import android.os.Bundle
//import timber.log.Timber
//
//class BackgroundDetector : Application.ActivityLifecycleCallbacks {
//    private var activeActivitiesCount = 0
//
//    override fun onActivityStarted(activity: Activity) {
//        activeActivitiesCount++
//        if (activeActivitiesCount == 1) {
//            Timber.d("Застосунок тепер ВИДИМИЙ на екрані (Foreground)")
//        }
//    }
//
//    override fun onActivityStopped(activity: Activity) {
//        activeActivitiesCount--
//        if (activeActivitiesCount == 0) {
//            Timber.d("Застосунок тепер у ФОНІ (Background)")
//        }
//    }
//
//    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
//    override fun onActivityResumed(activity: Activity) {}
//    override fun onActivityPaused(activity: Activity) {}
//    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
//    override fun onActivityDestroyed(activity: Activity) {}
//}

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber

class BackgroundDetector : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        Timber.d("Застосунок перейшов у FOREGROUND (видимий на екрані)")
    }

    override fun onStop(owner: LifecycleOwner) {
        Timber.d("Застосунок перейшов у BACKGROUND (згорнутий у фон)")
    }
}