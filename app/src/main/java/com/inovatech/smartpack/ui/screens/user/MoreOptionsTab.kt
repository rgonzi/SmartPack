package com.inovatech.smartpack.ui.screens.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.model.uiState.UserHomeUiState
import com.inovatech.smartpack.ui.items.MoreOptionItem

@Serializable
object MoreOptions

/**
 * Composable que defineix la pestanya d'altres opcions dins la pantalla d'inici de l'usuari
 */
@Composable
fun MoreOptionsTab(
    viewModel: UserHomeViewModel,
    uiState: UserHomeUiState,
    launchSnackbar: (String) -> Unit,
) {

    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading && uiState.msg != null) {
            launchSnackbar(uiState.msg)
            viewModel.resetMsg()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Més opcions",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        MoreOptionItem(
            title = "Factures"
        ) { /* TODO: Navegar a factures */ }
        Spacer(modifier = Modifier.height(8.dp))
    }
}