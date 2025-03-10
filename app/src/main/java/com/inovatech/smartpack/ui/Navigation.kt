package com.inovatech.smartpack.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inovatech.smartpack.ui.screens.Login
import com.inovatech.smartpack.ui.screens.LoginScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
) {
    val navController: NavHostController = rememberNavController()

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = modifier.fillMaxSize().padding(it)
        ) {
            composable<Login> {
                LoginScreen()
            }
        }
    }
}