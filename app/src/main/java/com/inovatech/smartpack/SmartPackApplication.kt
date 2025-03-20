package com.inovatech.smartpack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Punt d'entrada a la nostra aplicació necessari per configurar Hilt
 */
@HiltAndroidApp
class SmartPackApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}