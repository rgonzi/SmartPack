package com.inovatech.smartpack

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
import androidx.navigation.toRoute
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.ui.screens.*
import com.inovatech.smartpack.ui.screens.admin.AdminHome
import com.inovatech.smartpack.ui.screens.admin.AdminHomeScreen
import com.inovatech.smartpack.ui.screens.deliveryman.DeliverymanHome
import com.inovatech.smartpack.ui.screens.deliveryman.DeliverymanHomeScreen
import com.inovatech.smartpack.ui.screens.user.NewService
import com.inovatech.smartpack.ui.screens.user.NewServiceScreen
import com.inovatech.smartpack.ui.screens.user.UserHome
import com.inovatech.smartpack.ui.screens.user.UserHomeScreen
import com.inovatech.smartpack.ui.screens.userConfig.*
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
    //TODO: Posar una SplashScreen de debò
    LaunchedEffect(Unit) {
        delay(1000)
        startDestination = Login
    }
    AnimatedVisibility(
        startDestination == Splash, enter = fadeIn(), exit = fadeOut()
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            SplashScreen()
        }
    }
    AnimatedVisibility(
        startDestination != Splash, enter = fadeIn(), exit = fadeOut()
    ) {
        Surface {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize()
            ) {
                composable<Login> {
                    LoginScreen(
                        onNavigateUserHome = {
                            navController.navigate(UserHome) {
                                popUpTo(startDestination) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onNavigateDeliveryManHome = {
                            navController.navigate(DeliverymanHome) {
                                popUpTo(startDestination) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onNavigateAdminHome = {
                            navController.navigate(AdminHome) {
                                popUpTo(startDestination) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onRegisterClick = { navController.navigate(SignUp) },
                        onForgotPasswordClick = { navController.navigate(RememberPassword) })
                }
                composable<SignUp> {
                    SignUpScreen(
                        goToLoginScreen = { navController.popBackStack(Login, inclusive = false) })
                }
                composable<RememberPassword> {
                    RememberPasswordScreen(
                        onBackClick = { navController.popBackStack(Login, inclusive = false) })
                }
                composable<UserHome> {
                    UserHomeScreen(
                        navToConfig = {
                            navController.navigate(UserConfig)
                        },
                        navToNewService = {
                            navController.navigate(NewService)
                        }
                    )
                }
                composable<NewService> {
                    NewServiceScreen(
                        navigateUp = { navController.navigateUp() }
                    )

                }
                composable<UserConfig> {
                    UserConfigScreen(
                        onBackPressed = { navController.navigateUp() },
                        backToLogin = {
                            navController.navigate(Login) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onChangePassword = { id ->
                            navController.navigate(ChangePassword(id))
                        }
                    )
                }
                composable<ChangePassword> { backStackEntry ->
                    val args: ChangePassword = backStackEntry.toRoute()
                    ChangePasswordScreen(
                        userId = args.id,
                        onBackPressed = { navController.navigateUp() }
                    )
                }
                composable<DeliverymanHome> {
                    DeliverymanHomeScreen(
                        navToConfig = {
                            navController.navigate(UserConfig)
                        })
                }
                composable<AdminHome> {
                    AdminHomeScreen(
                        navToConfig = {
                            navController.navigate(UserConfig)
                        }
                    )
                }
            }
        }
    }
}
