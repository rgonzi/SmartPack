package com.inovatech.smartpack.ui.screens.user

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.Serializable
import androidx.compose.material3.Text

@Serializable
data object ActiveServices

@Composable
fun ActiveServicesTab(
    viewModel: UserConfigViewModel = hiltViewModel()
) {
    Text("Llista de serveis actius")
}