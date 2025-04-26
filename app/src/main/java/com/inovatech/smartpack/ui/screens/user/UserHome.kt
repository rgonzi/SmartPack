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
                //TODO Amagar FAB quan no estiguem a la pantalla de serveis actius
                Fab(onFabClick = navToNewService)
            },
            bottomBar = {
                HomeBottomBar(navToActiveServices = {
                    navController.navigate(ActiveServices) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }, navToOldServices = {
                    navController.navigate(OldServices) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }, navToMoreOptions = {
                    navController.navigate(MoreOptions) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                })
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
                        viewModel = viewModel, uiState = uiState, launchSnackbar = { msg ->
                            scope.launch {
                                snackbarHostState.showSnackbar(msg)
                            }
                        },
                        onNavToDetail = { serviceId ->
                            navController.navigate(ServiceItemDetailUser(serviceId))
                        }
                    )
                }
                composable<OldServices> {
                    OldServicesTab(
                        viewModel = viewModel, uiState = uiState, launchSnackbar = { msg ->
                            scope.launch {
                                snackbarHostState.showSnackbar(msg)
                            }
                        },
                        onNavToDetail = { serviceId ->
                            navController.navigate(ServiceItemDetailUser(serviceId))
                        }
                    )
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
    navToActiveServices: () -> Unit,
    navToOldServices: () -> Unit,
    navToMoreOptions: () -> Unit,
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Seguiment", "Històric", "Més opcions")
    val icons = listOf(
        painterResource(id = R.drawable.ic_package_box),
        painterResource(id = R.drawable.ic_historic_services),
        painterResource(id = R.drawable.ic_more_options_hor)
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(selected = selectedItem == index, onClick = {
                selectedItem = index
                when (index) {
                    0 -> navToActiveServices()
                    1 -> navToOldServices()
                    2 -> navToMoreOptions()
                }
            }, icon = {
                Icon(
                    icons[index], contentDescription = "", modifier = Modifier.size(24.dp)
                )
            }, label = { Text(items[index], textAlign = TextAlign.Center) })
        }
    }
}