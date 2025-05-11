package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.serialization.Serializable

@Serializable
object InvoicesTab

@Composable
fun InvoicesTab(
    snackbarHostState: SnackbarHostState,
) {
    Text(text = "Factures")
    LaunchedEffect(Unit) {
        snackbarHostState.showSnackbar("Pantalla no implementada")
    }
}