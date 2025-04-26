package com.inovatech.smartpack.ui.screens.deliveryman


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.R
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.uiState.DeliveryManUiState
import com.inovatech.smartpack.ui.items.ConfirmedServiceItemDelivery
import com.inovatech.smartpack.ui.theme.BlueSecondary
import kotlinx.serialization.Serializable

@Serializable
object ConfirmedDelivery

/**
 * Composable que defineix la pestanya de serveis finalitzats dins la pantalla d'inici del transportista
 */
@Composable
fun ConfirmedDeliveryTab(
    viewModel: DeliveryManHomeViewModel,
    uiState: DeliveryManUiState,
    launchSnackbar: (String) -> Unit,
) {
    val services = uiState.finalizedServices

    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading && uiState.msg != null) {
            launchSnackbar(uiState.msg)
            viewModel.resetMsg()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                "Serveis realitzats",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(services) { service ->
            ConfirmedServiceItemDelivery(
                service = service
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

