package com.inovatech.smartpack.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.R
import com.inovatech.smartpack.ui.theme.Background
import kotlinx.serialization.Serializable
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Serializable
data object UserHome

/**
 * Composable que defineix el disseny de la pantalla principal de l'app
 *
 * @param viewModel: El seu viewModel associat
 * @param backToLogin: Destí de navegació quan s'ha produït un logout i volem tornar
 * a la pantalla de Login
 */
@Composable
fun UserHomeScreen(
    viewModel: UserHomeViewModel = hiltViewModel(),
    backToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = { HomeTopAppBar() },
            floatingActionButton = { Fab() },
            bottomBar = {
                HomeBottomBar()
            }
        ) {
            LaunchedEffect(uiState.isLogoutSuccess) {
                if (uiState.isLogoutSuccess) {
                    backToLogin()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Benvingut a la app de SmartPack, usuari"
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error, contentColor = Color.Black
                    ),
                    onClick = { viewModel.logout() },
                ) {
                    Text("Tancar sessió")
                }

            }

            val navController = rememberNavController()

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
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
        )
    )
}

@Composable
fun Fab() {
    ExtendedFloatingActionButton(
        onClick = {  },

    ) {
        Icon(Icons.Default.Add, contentDescription = "Crear nou servei")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Nou servei")
    }
}

@Composable
fun HomeBottomBar(
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Seguiment", "Històric", "Configuració")
    val icons = listOf(Icons.Default.Search, Icons.Default.DateRange, Icons.Default.Settings)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = { Icon(icons[index], contentDescription = "") },
                label = { Text(items[index]) }
            )
        }
    }
}