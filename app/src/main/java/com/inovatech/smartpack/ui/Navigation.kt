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

/**
 * Composable que defineix un NavGraph per poder navegar per les diferents pantalles de l'aplicació
 *
 * @param tokenRepository: Quan es pugui validar la vigència d'un token al servidor, no caldrà iniciar
 * sessió cada vegada al obrir la app
 */
@Composable
fun Navigation(
    tokenRepository: TokenRepository,
) {
    val navController: NavHostController = rememberNavController()
    var startDestination by remember { mutableStateOf<Any>(Splash) }

    //Per quan poguem validar si el token és vàlid al servidor
    LaunchedEffect(Unit) {

        //Posem 1s de retard per si la petició és molt ràpida que
        //tinguem temps de mostrar la SplashScreen
        delay(1000)
        //startDestination = if (tokenRepository.isTokenValid()) Home else Login
        startDestination = Login
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
                        onLoginSuccess = {
                            navController.navigate(Home) {
                                popUpTo(startDestination) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onRegisterClick = { navController.navigate(SignUp) },
                        onForgotPasswordClick = { navController.navigate(RememberPassword) }
                    )
                }
                composable<SignUp> {
                    SignUpScreen(
                        goToLoginScreen = { navController.popBackStack(Login, inclusive = false) }
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
                        backToLogin = {
                            navController.navigate(Login) {
                                popUpTo(Home) { inclusive = true }
                            }

                        }
                    )
                }
            }
        }
    }
}
