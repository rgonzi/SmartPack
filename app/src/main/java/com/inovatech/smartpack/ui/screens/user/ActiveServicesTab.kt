package com.inovatech.smartpack.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.R
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.uiState.UserHomeUiState
import com.inovatech.smartpack.ui.items.DeleteDialog
import com.inovatech.smartpack.ui.items.ServiceItem
import kotlinx.serialization.Serializable

@Serializable
object ActiveServices

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
    //Controla si mostrem els botons de modificació/eliminació dels Items o no
    var expandedItemId by remember { mutableStateOf<Long?>(null) }
    //Llista dels serveis actius obtinguda de l'UIState
    val activeServices = uiState.activeServices
    //Controla si s'ha de mostrar un diàleg per modificar un servei o no (null)
    var serviceToModify by remember { mutableStateOf<Service?>(null) }
    //Controla el servei a eliminar
    var serviceToDelete by remember { mutableStateOf<Service?>(null) }

    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading && uiState.msg != null) {
            launchSnackbar(uiState.msg)
            viewModel.resetMsg()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshServices()
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshServices() },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()
        ) {
            if (activeServices.isNotEmpty() && !uiState.isLoading) {
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
                            expandedItemId = if (expandedItemId == service.id) null else service.id
                        },
                        onDeleteClick = { serviceToDelete = service },
                        onModifyServiceClick = { serviceToModify = service },
                        onNavToDetail = { onNavToDetail(service.id) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
                //TODO: Afegir botó per pagar el servei
            } else if (!uiState.isLoading && uiState.hasLoadedOnce) {
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

    //AlertDialog que mostra i modifica les dades del servei en qüestió
    serviceToModify?.let { service ->
        ServiceDetailsDialog(
            service = service,
            onDismiss = {
                serviceToModify = null
                expandedItemId = null
            },
            onUpdate = { viewModel.updateService(it) }
        )
    }

    //AlertDialog per confirmar la eliminació del servei
    serviceToDelete?.let { service ->
        DeleteDialog(
            onDismiss = { serviceToDelete = null },
            text = "Segur que vols eliminar el servei? Aquesta acció és irreversible",
            onConfirm = {
                viewModel.deleteService(service.id)
                serviceToDelete = null
                expandedItemId = null
            }
        )
    }
}

@Composable
fun ServiceDetailsDialog(
    service: Service,
    onDismiss: () -> Unit,
    onUpdate: (Service) -> Unit,
) {
    var newRecipientName by remember { mutableStateOf(service.packageToDeliver.recipientName) }
    var newRecipientAddress by remember { mutableStateOf(service.packageToDeliver.recipientAddress) }
    var newRecipientPhone by remember { mutableStateOf(service.packageToDeliver.recipientPhone) }
    var newDimensions by remember { mutableStateOf(service.packageToDeliver.dimensions) }
    var newWeight by remember { mutableIntStateOf(service.packageToDeliver.weight) }
    var newDetails by remember { mutableStateOf(service.packageToDeliver.details) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modificar servei") },
        text = {
            Column {
                OutlinedTextField(
                    value = newRecipientName,
                    onValueChange = { newRecipientName = it },
                    label = { Text("Nom destinatari") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = newRecipientAddress,
                    onValueChange = { newRecipientAddress = it },
                    label = { Text("Adreça destinatari") },
                    minLines = 2,
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = newRecipientPhone,
                    onValueChange = { newRecipientPhone = it },
                    label = { Text("Telèfon destinatari") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = newDimensions, onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            newDimensions = it
                        }
                    }, label = { Text("Dimesions totals") }, trailingIcon = {
                        Text(text = "cm", style = MaterialTheme.typography.bodyMedium)
                    }, singleLine = true
                )
                OutlinedTextField(
                    value = newWeight.toString(), onValueChange = {
                        if (it.isEmpty()) {
                            newWeight = 1
                        } else if (it.all { char -> char.isDigit() }) {
                            newWeight = it.toInt()
                        }
                    }, label = { Text("Pes") }, trailingIcon = {
                        Text(text = "kg", style = MaterialTheme.typography.bodyMedium)
                    }, singleLine = true
                )
                OutlinedTextField(
                    value = newDetails,
                    onValueChange = { newDetails = it },
                    label = { Text("Detalls") },
                    minLines = 2,
                    maxLines = 2,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(
                        service.copy(
                            packageToDeliver = service.packageToDeliver.copy(
                                recipientName = newRecipientName,
                                recipientAddress = newRecipientAddress,
                                recipientPhone = newRecipientPhone,
                                weight = newWeight,
                                dimensions = newDimensions,
                            )
                        )
                    )
                    onDismiss()
                }
            ) { Text("Modificar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar")
            }
        })
}

@Preview
@Composable
fun ServiceDetailsDialogPreview() {
    ServiceDetailsDialog(
        service = Service(),
        onDismiss = { },
        onUpdate = { }
    )
}