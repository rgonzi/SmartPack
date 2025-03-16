package com.inovatech.smartpack.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.ui.screens.*

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
) {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    val activity = context as Activity
    val storage = TokenRepository(context)
    var startDestination by remember { mutableStateOf<Any>(Login) }

    LaunchedEffect(Unit) {
        val isValid = storage.isTokenValid()
        startDestination = if (isValid) Home else Login
    }

    Surface {
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = modifier.fillMaxSize()
        ) {
            composable<Login> {
                LoginScreen(
                    onRegisterClick = { navController.navigate(SignUp) },
                    onForgotPasswordClick = { navController.navigate(RememberPassword) }
                )
            }
            composable<SignUp> {
                SignUpEmailScreen(
                    onNextClick = { /*TODO: Mostrar missatge de confirmació i registrar-se*/ },
                    onCancelClick = { navController.popBackStack(Login, inclusive = false) }
                )
            }
            composable<RememberPassword> {
                RememberPasswordScreen(
                    onNextClick = {/* TODO: Implementar recordar contrasenya */ },
                    onCancelClick = { navController.popBackStack(Login, inclusive = false) }
                )
            }
            composable<Home> {
                HomeScreen(
                    backToLogin = { navController.popBackStack(Login, inclusive = false) }
                )
            }
        }
    }
}