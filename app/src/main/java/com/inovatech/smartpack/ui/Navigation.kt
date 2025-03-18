package com.inovatech.smartpack.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.ui.screens.*
import kotlinx.coroutines.delay

@Composable
fun Navigation(
    storage: TokenRepository,
) {
    val navController: NavHostController = rememberNavController()
    var startDestination by remember { mutableStateOf<Any>(Splash) }

    LaunchedEffect(Unit) {
        delay(1500)
        val isValid = storage.isTokenValid()
        startDestination = if (isValid) Home else Login
    }
    AnimatedVisibility(
        startDestination == Splash,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            SplashScreen()
        }
    }
    AnimatedVisibility(
        startDestination != Splash,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize()
            ) {
                composable<Login> {
                    LoginScreen(
                        onRegisterClick = { navController.navigate(SignUp) },
                        onForgotPasswordClick = { navController.navigate(RememberPassword) }
                    )
                }
                composable<SignUp> {
                    SignUpScreen(
                        onNextClick = { navController.popBackStack(Login, inclusive = false) },
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
}
