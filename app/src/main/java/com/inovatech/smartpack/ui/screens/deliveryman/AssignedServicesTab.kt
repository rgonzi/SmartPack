package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.R
import com.inovatech.smartpack.model.uiState.DeliveryManUiState
import com.inovatech.smartpack.ui.items.ServiceItemDelivery
import kotlinx.serialization.Serializable

@Serializable
object AssignedServices

/**
 * Composable que defineix la pestanya de serveis assignats dins la pantalla d'inici del transportista
 */
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
        modifier = Modifier.fillMaxSize(),
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshServices() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (services.isNotEmpty()) {
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
                        onStatusChange = { status -> viewModel.changeStatus(service.id, status) },
                        onConfirmDelivery = { recipientPhone ->
                            viewModel.confirmDelivery(service.id, recipientPhone)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                item {
                    Text(
                        "No hi ha cap servei assignat",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(64.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_nothing_here),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
    }
}

