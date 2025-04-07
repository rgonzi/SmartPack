package com.inovatech.smartpack.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inovatech.smartpack.R
import com.inovatech.smartpack.ui.theme.Background
import kotlinx.serialization.Serializable
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Serializable
data object UserHome

/**
 * Composable que defineix el disseny de la pantalla principal de l'app
 *
 * @param viewModel: El seu viewModel associat
 * a la pantalla de Login
 */
@Composable
fun UserHomeScreen(
//    viewModel: UserHomeViewModel = hiltViewModel(),
    navToConfig: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                HomeTopAppBar(navToConfig = navToConfig)
            },
            floatingActionButton = {
                if (navController.currentDestination == ActiveServices) {
                    Fab()
                }
            },
            bottomBar = {
                HomeBottomBar(
                    navToActiveServices = {
                        navController.navigate(ActiveServices) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    navToOldServices = {
                        navController.navigate(OldServices) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    navToMoreOptions = {
                        navController.navigate(MoreOptions) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = ActiveServices,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(32.dp)
            ) {
                composable<ActiveServices> {
                    ActiveServicesTab()
                }
                composable<OldServices> {
                    OldServicesTab()
                }
                composable<MoreOptions> {
                    MoreOptionsTab()
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    navToConfig: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.Black
        ),
        actions = {
            IconButton(onClick = { navToConfig() }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Configuració del compte", modifier = Modifier.fillMaxSize())
            }
        }
    )
}

@Composable
fun Fab() {
    ExtendedFloatingActionButton(
        onClick = { },

        ) {
        Icon(Icons.Default.Add, contentDescription = "Crear nou servei")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Nou servei")
    }
}

@Composable
fun HomeBottomBar(
    navToActiveServices: () -> Unit,
    navToOldServices: () -> Unit,
    navToMoreOptions: () -> Unit,
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Seguiment", "Històric", "Més opcions")
    val icons = listOf(Icons.Default.Search, Icons.Default.DateRange, Icons.Default.MoreVert)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    when (index) {
                        0 -> navToActiveServices()
                        1 -> navToOldServices()
                        2 -> navToMoreOptions()
                    }
                },
                icon = { Icon(icons[index], contentDescription = "") },
                label = { Text(items[index]) }
            )
        }
    }
}