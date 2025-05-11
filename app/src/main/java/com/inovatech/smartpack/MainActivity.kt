package com.inovatech.smartpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.ui.theme.SmartPackTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var storage: TokenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Cridem a la SplashScreen
        installSplashScreen()
        setContent {
            SmartPackTheme {
                Navigation(storage)
            }
        }
    }

    /**
     * Ens assegurem que al tancar l'app eliminem el token de les SharedPreferences
     */
    override fun onDestroy() {
        super.onDestroy()
        storage.clearAuthToken()
    }
}
