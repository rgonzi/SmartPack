package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.R
import com.inovatech.smartpack.ui.theme.Background
import kotlinx.serialization.Serializable

@Serializable
data object DeliverymanHome

@Composable
fun DeliverymanHomeScreen(
    viewModel: DeliveryManHomeViewModel = hiltViewModel(),
    backToLogin: () -> Unit,
    onSettingsClick: () -> Unit = {} //TODO Implementar pantalla Settings
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
            topBar = { HomeTopAppBar(onSettingsClick) }
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
                    "Benvingut a la app de SmartPack, transportista"
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

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Aquí aniria la llista de serveis per repartir"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onSettingsClick: () -> Unit
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
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = ""
                )
            }
        }
    )
}