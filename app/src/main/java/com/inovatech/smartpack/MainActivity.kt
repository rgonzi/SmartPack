package com.inovatech.smartpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.ui.Navigation
import com.inovatech.smartpack.ui.theme.SmartPackTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var storage: TokenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartPackTheme {
                Navigation(storage)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        storage.clearAuthToken()
    }
}
