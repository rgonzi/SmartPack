package com.inovatech.smartpack.ui.screens.user

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
import com.inovatech.smartpack.model.uiState.UserHomeUiState
import com.inovatech.smartpack.ui.items.ServiceItem
import kotlinx.serialization.Serializable

@Serializable
data object ActiveServices

/**
 * Composable que defineix la pestanya de serveis assignats dins la pantalla d'inici de l'usuari
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveServicesTab(
    viewModel: UserHomeViewModel,
    uiState: UserHomeUiState,
    launchSnackbar: (String) -> Unit,
    onNavToDetail: (Long) -> Unit,
) {
    var expandedItemId by remember { mutableStateOf<Long?>(null) }
    val activeServices = uiState.activeServices

    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading && uiState.msg != null) {
            launchSnackbar(uiState.msg)
            viewModel.resetMsg()
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshServices() },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (activeServices.isNotEmpty()) {
                item {
                    Text(
                        "Serveis actius",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(activeServices) { service ->
                    ServiceItem(
                        service = service,
                        isExpanded = expandedItemId == service.id,
                        onClick = {
                            expandedItemId =
                                if (expandedItemId == service.id) null else service.id
                        },
                        onDeleteClick = { viewModel.deleteService(service.id) },
                        onModifyServiceClick = { /* TODO Modificar servei per usuari */ },
                        onNavToDetail = { onNavToDetail(service.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                item {
                    Text(
                        "No hi ha cap servei actiu",
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