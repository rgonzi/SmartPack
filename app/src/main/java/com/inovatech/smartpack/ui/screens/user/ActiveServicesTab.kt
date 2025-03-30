package com.inovatech.smartpack.ui.screens.user

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.Serializable

@Serializable
data object ActiveServices

@Composable
fun ActiveServicesTab(
    viewModel: UserHomeViewModel = hiltViewModel()
) {

}