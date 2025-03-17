package com.inovatech.smartpack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartPackApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}