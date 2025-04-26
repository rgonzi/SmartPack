package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
data object AdminHome

/**
 * Composable que defineix el disseny de la pantalla d'inici d'usuari admin.
 */
@Composable
fun AdminHomeScreen(
    viewModel: AdminHomeViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit = {} //TODO Implementar pantalla Settings
    ) {
    //TODO Refer tota la pantalla que s'assembli a la del Usuari
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Benvingut a la app de SmartPack, ADMIN"
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error, contentColor = Color.Black
                        ),
                        onClick = {  },
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