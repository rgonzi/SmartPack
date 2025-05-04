package com.inovatech.smartpack.ui.screens.admin

import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
data object AdminHome

/**
 * Composable que defineix el disseny de la pantalla d'inici d'usuari admin.
 */
@Composable
fun AdminHomeScreen(
    viewModel: AdminHomeViewModel = hiltViewModel(),
    navToConfig: () -> Unit,
    navToNewService: () -> Unit,
    navToNewVehicle: () -> Unit,
    //TODO Afegir les pantalles de creació que falten
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf<AdminHomeTab>(AdminHomeTab.ServicesTabDestAdmin) }

    var fabExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading) {
            uiState.msg?.let {
                snackbarHostState.showSnackbar(it)
                viewModel.resetMsg()
            }
        }
    }

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
                AdminFab(
                    expanded = fabExpanded,
                    onFabClick = { fabExpanded = !fabExpanded },
                    //TODO: Crear pantalles per afegir nous objectes
                    onCreateUser = { },
                    onCreateCompany = { },
                    onCreateService = { navToNewService() },
                    onCreateVehicle = { navToNewVehicle() })
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
                        viewModel = viewModel, uiState = uiState, launchSnackbar = { msg ->
                            scope.launch {
                                snackbarHostState.showSnackbar(msg)
                            }
                        })
                }
                composable<CompanyTab> {
                    CompanyTab(
                        viewModel = viewModel, uiState = uiState
                    )
                }
                composable<UsersTab> {
                    UsersTab(
                        viewModel = viewModel, uiState = uiState
                    )
                }
                composable<InvoicesTab> {
                    InvoicesTab()
                }
                composable<VehiclesTab> {
                    VehiclesTab(
                        viewModel = viewModel, uiState = uiState
                    )
                }
            }

            //Scrim que enfosqueix la pantalla quan tenim el menú del FAB obert
            AnimatedVisibility(
                visible = fabExpanded,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000))
                    .clickable { fabExpanded = false }) {}
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AdminFab(
    expanded: Boolean,
    onFabClick: () -> Unit,
    onCreateUser: () -> Unit,
    onCreateCompany: () -> Unit,
    onCreateService: () -> Unit,
    onCreateVehicle: () -> Unit,
) {
    val rotation by animateFloatAsState(if (expanded) 45f else 0f)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(tween(120)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(120)
                ) + slideInVertically(
                    initialOffsetY = { it / 2 }, animationSpec = tween(120)
                ),
                exit = fadeOut(tween(80)) + scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(80)
                ) + slideOutVertically(
                    targetOffsetY = { it / 2 }, animationSpec = tween(80)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FabMenuItem(
                        text = "Nou servei", iconRes = R.drawable.ic_package_box
                    ) {
                        onCreateService()
                        onFabClick()
                    }
                    FabMenuItem(
                        text = "Nova empresa", iconRes = R.drawable.ic_business
                    ) {
                        onCreateCompany()
                        onFabClick()
                    }
                    FabMenuItem(
                        text = "Nou usuari", iconRes = R.drawable.ic_account_circle
                    ) {
                        onCreateUser()
                        onFabClick()
                    }
                    FabMenuItem(
                        text = "Nou vehicle", iconRes = R.drawable.ic_delivery_truck
                    ) {
                        onCreateVehicle()
                        onFabClick()
                    }
                }
            }

            FloatingActionButton(
                onClick = onFabClick,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = if (expanded) "Tancar" else "Crear",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun FabMenuItem(text: String, @DrawableRes iconRes: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier
            .wrapContentWidth()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = text, style = MaterialTheme.typography.bodyMedium, color = Color.White
        )
        Spacer(Modifier.width(12.dp))
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview
@Composable
fun FabMenuItemPreview() {
    FabMenuItem(
        text = "Nou usuari", iconRes = R.drawable.ic_account_circle
    ) { }
}


@Composable
fun HomeBottomBar(
    currentTab: AdminHomeTab,
    onTabSelected: (AdminHomeTab) -> Unit,
) {
    val items = listOf(
        Triple(
            AdminHomeTab.ServicesTabDestAdmin, painterResource(R.drawable.ic_package_box), "Serveis"
        ), Triple(
            AdminHomeTab.CompanyTabDestAdmin, painterResource(R.drawable.ic_business), "Empreses"
        ), Triple(
            AdminHomeTab.UsersTabDestAdmin, painterResource(R.drawable.ic_happy), "Usuaris"
        ), Triple(
            AdminHomeTab.InvoicesTabDestAdmin, painterResource(R.drawable.ic_invoice), "Factures"
        ), Triple(
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