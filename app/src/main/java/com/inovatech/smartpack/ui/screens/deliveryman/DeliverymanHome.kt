package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inovatech.smartpack.R
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.theme.Background
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object DeliverymanHome

@Composable
fun DeliverymanHomeScreen(
    viewModel: DeliveryManHomeViewModel = hiltViewModel(),
    navToConfig: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                HomeTopAppBar(navToConfig = navToConfig)
            },
            bottomBar = {
                HomeBottomBar(
                    navToAssignedServices = {
                        navController.navigate(AssignedServices) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    navToConfirmDelivery = {
                        navController.navigate(ConfirmDelivery) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    navToVehicles = {
                        navController.navigate(Vehicles) {
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
                startDestination = AssignedServices,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(32.dp)
            ) {
                composable<AssignedServices> {
                    AssignedServicesTab()
                }
                composable<ConfirmDelivery> {
                    ConfirmDeliveryTab()
                }
                composable<Vehicles> {
                    VehiclesTab(
                        viewModel = viewModel,
                        uiState = uiState,
                        launchSnackbar = { msg ->
                            scope.launch {
                                snackbarHostState.showSnackbar(msg)
                            }
                        }
                    )
                }
            }

        }
    }
    LoadingScreen(uiState.isLoading)
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
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Configuració del compte",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}

@Composable
fun HomeBottomBar(
    navToAssignedServices: () -> Unit,
    navToConfirmDelivery: () -> Unit,
    navToVehicles: () -> Unit,
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Serveis assignats", "Confirmar entrega", "Vehicle i llicències")
    val icons = listOf(Icons.Default.Email, Icons.Default.CheckCircle, Icons.Default.Star)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    when (index) {
                        0 -> navToAssignedServices()
                        1 -> navToConfirmDelivery()
                        2 -> navToVehicles()
                    }
                },
                icon = { Icon(icons[index], contentDescription = "") },
                label = { Text(items[index]) }
            )
        }
    }
}