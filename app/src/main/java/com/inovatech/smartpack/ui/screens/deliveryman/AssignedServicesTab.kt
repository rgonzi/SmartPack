package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.inovatech.smartpack.model.Package
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.ServiceStatus
import com.inovatech.smartpack.model.uiState.DeliveryManUiState
import com.inovatech.smartpack.ui.theme.BlueSecondary
import com.inovatech.smartpack.ui.theme.StatusDelivered
import com.inovatech.smartpack.ui.theme.StatusNotDelivered
import com.inovatech.smartpack.ui.theme.StatusReturned
import kotlinx.serialization.Serializable

@Serializable
object AssignedServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignedServicesTab(
    viewModel: DeliveryManHomeViewModel,
    uiState: DeliveryManUiState,
    launchSnackbar: (String) -> Unit,
    onNavToDetail: (Long) -> Unit,
) {
    var expandedItemId by remember { mutableStateOf<Long?>(null) }
    val services = uiState.assignedServices

    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading && uiState.msg != null) {
            launchSnackbar(uiState.msg)
            viewModel.resetMsg()
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshServices()}
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    "Serveis per entregar",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(services) { service ->
                ServiceItemDelivery(
                    service = service,
                    isExpanded = expandedItemId == service.id,
                    onClick = {
                        expandedItemId = if (expandedItemId == service.id) null else service.id
                    },
                    onNavToDetail = { onNavToDetail(service.id) },
                    onStatusChange = { status -> viewModel.changeStatus(service.id, status) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun ServiceItemDelivery(
    service: Service,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onNavToDetail: () -> Unit,
    onStatusChange: (ServiceStatus) -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(color = BlueSecondary)
            .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_package_box),
                contentDescription = "Paquet",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(4.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.packageToDeliver.recipientName,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = service.packageToDeliver.recipientAddress,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    maxLines = 2
                )
            }

            IconButton(onClick = onNavToDetail) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusButton("Entregat", color = StatusDelivered) {
                    onStatusChange(ServiceStatus.ENTREGAT)
                }
                StatusButton("Absent", color = StatusNotDelivered) {
                    onStatusChange(ServiceStatus.NO_ENTREGAT)
                }
                StatusButton("Retornat", color = StatusReturned) {
                    onStatusChange(ServiceStatus.RETORNAT)
                }
            }
        }
    }
}
@Composable
fun StatusButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, color = Color.White, fontSize = 13.sp)
    }
}

