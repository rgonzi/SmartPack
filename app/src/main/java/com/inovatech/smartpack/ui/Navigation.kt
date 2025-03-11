package com.inovatech.smartpack.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inovatech.smartpack.ui.screens.Login
import com.inovatech.smartpack.ui.screens.LoginScreen
import com.inovatech.smartpack.ui.screens.SignUp.SignUpEmail
import com.inovatech.smartpack.ui.screens.SignUp.SignUpEmailScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
) {
    val navController: NavHostController = rememberNavController()

    Surface {
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = modifier.fillMaxSize()
        ) {
            composable<Login> {
                LoginScreen(
                    onRegisterClick = {navController.navigate(SignUpEmail)}
                )
            }
            composable<SignUpEmail> {
                SignUpEmailScreen()
            }
        }
    }
}