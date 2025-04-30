package com.inovatech.smartpack.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.screens.deliveryman.HomeBottomBar
import kotlinx.coroutines.launch

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
    viewModel: UserHomeViewModel = hiltViewModel(),
    navToConfig: () -> Unit,
    navToNewService: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var currentTab by remember { mutableStateOf<HomeTab>(HomeTab.ActiveServicesTabDest) }

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
            floatingActionButton = {
                if (currentTab is HomeTab.ActiveServicesTabDest) {
                    Fab(onFabClick = navToNewService)
                }
            },
            bottomBar = {
                HomeBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        currentTab = tab
                        when (tab) {
                            HomeTab.ActiveServicesTabDest -> navController.navigate(ActiveServices) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }

                            HomeTab.OldServicesTabDest -> navController.navigate(OldServices) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }

                            HomeTab.MoreOptionsTabDest -> navController.navigate(MoreOptions) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }) {
            NavHost(
                navController = navController,
                startDestination = ActiveServices,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                composable<ActiveServices> {
                    ActiveServicesTab(
                        viewModel = viewModel,
                        uiState = uiState,
                        launchSnackbar = { msg ->
                            scope.launch {
                                snackbarHostState.showSnackbar(msg)
                            }
                        },
                        onNavToDetail = { serviceId ->
                            navController.navigate(ServiceItemDetailUser(serviceId))
                        })
                }
                composable<OldServices> {
                    OldServicesTab(
                        viewModel = viewModel,
                        uiState = uiState,
                        launchSnackbar = { msg ->
                            scope.launch {
                                snackbarHostState.showSnackbar(msg)
                            }
                        },
                        onNavToDetail = { serviceId ->
                            navController.navigate(ServiceItemDetailUser(serviceId))
                        })
                }
                composable<ServiceItemDetailUser> { backStackEntry ->
                    val args: ServiceItemDetailUser = backStackEntry.toRoute()
                    ServiceItemDetailUserScreen(
                        viewModel = viewModel,
                        uiState = uiState,
                        serviceId = args.serviceId,
                        launchSnackbar = { msg ->
                            scope.launch {
                                snackbarHostState.showSnackbar(msg)
                            }
                        },
                        onBackPressed = { navController.navigateUp() })
                }
                composable<MoreOptions> {
                    MoreOptionsTab()
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
fun Fab(
    onFabClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        onClick = onFabClick
    ) {
        Icon(Icons.Default.Add, contentDescription = "Crear nou servei")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Enviar un paquet")
    }
}

@Composable
fun HomeBottomBar(
    currentTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    val items = listOf(
        Triple(HomeTab.ActiveServicesTabDest, painterResource(R.drawable.ic_package_box), "Seguiment"),
        Triple(HomeTab.OldServicesTabDest,    painterResource(R.drawable.ic_historic_services), "Finalitzats"),
        Triple(HomeTab.MoreOptionsTabDest,    painterResource(R.drawable.ic_more_options_hor), "Més opcions")
    )

    NavigationBar {
        items.forEach { (tab, icon, label) ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon    = { Icon(icon, contentDescription = null, Modifier.size(24.dp)) },
                label   = { Text(label, textAlign = TextAlign.Center) }
            )
        }
    }
}

sealed interface HomeTab {
    @Serializable
    object ActiveServicesTabDest : HomeTab

    @Serializable
    object OldServicesTabDest : HomeTab

    @Serializable
    object MoreOptionsTabDest : HomeTab
}