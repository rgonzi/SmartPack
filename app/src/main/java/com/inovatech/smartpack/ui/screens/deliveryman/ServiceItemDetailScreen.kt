package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
    onBackPressed: () -> Unit
) {
    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading && uiState.msg != null) {
            launchSnackbar(uiState.msg)
            viewModel.resetMsg()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Detall del servei",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        ServiceItemDetail(service = uiState.assignedServices.find { it.id == serviceId }!!)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray, contentColor = Color.White
            ), onClick = { onBackPressed() }) {
            Text("Tornar")
        }
    }
}