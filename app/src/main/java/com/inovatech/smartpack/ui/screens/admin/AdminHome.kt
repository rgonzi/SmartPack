package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inovatech.smartpack.R
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.screens.user.Fab
import com.inovatech.smartpack.ui.screens.user.UserHomeTab
import com.inovatech.smartpack.ui.theme.Background
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object AdminHome

/**
 * Composable que defineix el disseny de la pantalla d'inici d'usuari admin.
 */
@Composable
fun AdminHomeScreen(
    viewModel: AdminHomeViewModel = hiltViewModel(),
    navToConfig: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf<AdminHomeTab>(AdminHomeTab.ServicesTabDestAdmin) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = { HomeTopAppBar(navToConfig) },
            floatingActionButton = {
                if (currentTab is AdminHomeTab.ServicesTabDestAdmin) {
                    FloatingActionButton(
                        onClick = { /*TODO: Crear un usuari*/ }
                    ) { Icon(Icons.Default.Add, contentDescription = null) }
                }
            },
            bottomBar = {
                HomeBottomBar(
                    currentTab = currentTab, onTabSelected = { tab ->
                        currentTab = tab
                        when (tab) {
                            AdminHomeTab.ServicesTabDestAdmin -> navController.navigate(
                                ServicesTab
                            ) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }

                            AdminHomeTab.CompanyTabDestAdmin -> navController.navigate(
                                CompanyTab
                            ) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }

                            AdminHomeTab.UsersTabDestAdmin -> navController.navigate(
                                UsersTab
                            ) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }

                            AdminHomeTab.InvoicesTabDestAdmin -> navController.navigate(
                                InvoicesTab
                            ) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }

                            AdminHomeTab.VehiclesTabDestAdmin -> navController.navigate(
                                VehiclesTab
                            ) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    })
            }) {
            NavHost(
                navController = navController,
                startDestination = ServicesTab,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                composable<ServicesTab> {
                    ServicesTab(
                        viewModel = viewModel,
                        uiState = uiState,
                        launchSnackbar = { msg ->
                            scope.launch {
                                snackbarHostState.showSnackbar(msg)
                            }
                        }
                    )
                }
                composable<CompanyTab> {
                    CompanyTab()
                }
                composable<UsersTab> {
                    UsersTab(
                        viewModel = viewModel,
                        uiState = uiState,
                        launchSnackbar = { msg ->
                            scope.launch {
                                snackbarHostState.showSnackbar(msg)
                            }
                        }
                    )
                }
                composable<InvoicesTab> {
                    InvoicesTab()
                }
                composable<VehiclesTab> {
                    VehiclesTab()
                }
            }
        }
        LoadingScreen(uiState.isLoading)
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
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent, titleContentColor = Color.Black
        ), actions = {
            IconButton(onClick = navToConfig) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Configuració del compte",
                    modifier = Modifier.fillMaxSize()
                )
            }
        })
}

@Composable
fun HomeBottomBar(
    currentTab: AdminHomeTab,
    onTabSelected: (AdminHomeTab) -> Unit,
) {
    val items = listOf(
        Triple(
            AdminHomeTab.ServicesTabDestAdmin, painterResource(R.drawable.ic_package_box), "Serveis"
        ),
        Triple(
            AdminHomeTab.CompanyTabDestAdmin,
            painterResource(R.drawable.ic_business),
            "Empreses"
        ),
        Triple(
            AdminHomeTab.UsersTabDestAdmin,
            painterResource(R.drawable.ic_happy),
            "Usuaris"
        ),
        Triple(
            AdminHomeTab.InvoicesTabDestAdmin,
            painterResource(R.drawable.ic_invoice),
            "Factures"
        ),
        Triple(
            AdminHomeTab.VehiclesTabDestAdmin,
            painterResource(R.drawable.ic_delivery_truck),
            "Vehicles"
        )
    )

    NavigationBar {
        items.forEach { (tab, icon, label) ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon = { Icon(icon, contentDescription = null, Modifier.size(24.dp)) },
                label = { Text(label, textAlign = TextAlign.Center) })
        }
    }
}

sealed interface AdminHomeTab {
    @Serializable
    object ServicesTabDestAdmin : AdminHomeTab

    @Serializable
    object CompanyTabDestAdmin : AdminHomeTab

    @Serializable
    object UsersTabDestAdmin : AdminHomeTab

    @Serializable
    object InvoicesTabDestAdmin : AdminHomeTab

    @Serializable
    object VehiclesTabDestAdmin : AdminHomeTab
}