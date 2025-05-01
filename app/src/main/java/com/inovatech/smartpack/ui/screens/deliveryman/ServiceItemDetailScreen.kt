package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.model.uiState.DeliveryManUiState
import com.inovatech.smartpack.ui.items.ServiceHistoricItem
import com.inovatech.smartpack.ui.items.ServiceItemDetail
import kotlinx.serialization.Serializable

@Serializable
data class ServiceItemDetailDeliveryman(val serviceId: Long)

@Composable
fun ServiceItemDetailDeliverymanScreen(
    viewModel: DeliveryManHomeViewModel,
    uiState: DeliveryManUiState,
    serviceId: Long,
    launchSnackbar: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val historic = uiState.serviceHistory

    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading && uiState.msg != null) {
            launchSnackbar(uiState.msg)
            viewModel.resetMsg()
        }
    }

    //Al composar la pantalla, obtindrem l'historic dels canvis d'estat del servei en qüestió
    LaunchedEffect(Unit) {
        viewModel.getServiceHistoryDetail(serviceId)
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                "Detall del servei",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            ServiceItemDetail(service = uiState.assignedServices.find { it.id == serviceId }!!)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Historial de seguiment",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(historic) { event ->
            ServiceHistoricItem(
                event = event,
                historic = historic
            )

        }
        item {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray, contentColor = Color.White
                ), onClick = { onBackPressed() }) {
                Text("Tornar")
            }
        }
    }
}