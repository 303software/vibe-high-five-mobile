package com.highfive.app.android

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.highfive.app.networking.NetworkClient
import com.highfive.app.storage.LocalStore

class HighFiveApplication  : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(HighFiveActivityLifecycleCallbacks())
        instance = this
        localStore = LocalStore()
        networkClient = NetworkClient()
    }

    inner class HighFiveActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        override fun onActivityStarted(p0: Activity) {
        }

        override fun onActivityResumed(p0: Activity) {
        }

        override fun onActivityPaused(p0: Activity) {
        }

        override fun onActivityStopped(p0: Activity) {
        }

        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        }

        override fun onActivityDestroyed(p0: Activity) {
        }
    }

    companion object {
        lateinit var instance: HighFiveApplication
        lateinit var localStore: LocalStore
        lateinit var networkClient: NetworkClient
    }
}