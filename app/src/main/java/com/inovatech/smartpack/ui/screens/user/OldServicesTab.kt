package com.inovatech.smartpack.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
object OldServices

/**
 * Composable que defineix la pestanya de serveis antics dins la pantalla d'inici de l'usuari
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OldServicesTab(
    viewModel: UserHomeViewModel,
    uiState: UserHomeUiState,
    launchSnackbar: (String) -> Unit,
    onNavToDetail: (Long) -> Unit,
) {
    val oldServices = uiState.oldServices

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

            if (oldServices.isNotEmpty()) {
                item {
                    Text(
                        "Serveis finalitzats",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(oldServices) { service ->
                    ServiceItem(
                        service = service,
                        onNavToDetail = { onNavToDetail(service.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                item {
                    Text(
                        "No tens serveis finalitzats",
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