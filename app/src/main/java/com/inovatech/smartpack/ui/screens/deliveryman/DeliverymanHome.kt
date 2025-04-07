package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inovatech.smartpack.R
import com.inovatech.smartpack.ui.screens.user.ActiveServices
import com.inovatech.smartpack.ui.screens.user.ActiveServicesTab
import com.inovatech.smartpack.ui.screens.user.MoreOptions
import com.inovatech.smartpack.ui.screens.user.MoreOptionsTab
import com.inovatech.smartpack.ui.screens.user.OldServices
import com.inovatech.smartpack.ui.screens.user.OldServicesTab
import com.inovatech.smartpack.ui.theme.Background
import kotlinx.serialization.Serializable

@Serializable
data object DeliverymanHome

@Composable
fun DeliverymanHomeScreen(
    viewModel: DeliveryManHomeViewModel = hiltViewModel(),
    navToConfig: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

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
//            LaunchedEffect(uiState.msg) {
//                if (!uiState.isLoading) {
//                    if (uiState.msg != null) {
//                        snackbarHostState.showSnackbar(uiState.msg!!)
//                        viewModel.resetMsg()
//                    }
//                }
//            }

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
                        uiState = uiState
                    )
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