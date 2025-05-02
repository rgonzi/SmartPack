package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.inovatech.smartpack.model.uiState.AdminHomeUiState
import kotlinx.serialization.Serializable

@Serializable
object ServicesTab

@Composable
fun ServicesTab(
    viewModel: AdminHomeViewModel,
    uiState: AdminHomeUiState,
    launchSnackbar: (String) -> Unit,
) {

    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading && uiState.msg != null) {
            launchSnackbar(uiState.msg)
            viewModel.resetMsg()
        }
    }


}