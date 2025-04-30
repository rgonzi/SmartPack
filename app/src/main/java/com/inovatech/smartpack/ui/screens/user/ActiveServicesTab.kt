package com.inovatech.smartpack.ui.screens.user

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.R
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.uiState.UserHomeUiState
import com.inovatech.smartpack.ui.LoadingScreen
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
        var newRecipientName by remember { mutableStateOf(service.packageToDeliver.recipientName) }
        var newRecipientAddress by remember { mutableStateOf(service.packageToDeliver.recipientAddress) }
        var newRecipientPhone by remember { mutableStateOf(service.packageToDeliver.recipientPhone) }
        var newDimensions by remember { mutableStateOf(service.packageToDeliver.dimensions) }
        var newWeight by remember { mutableIntStateOf(service.packageToDeliver.weight) }
        var newDetails by remember { mutableStateOf(service.packageToDeliver.details) }

        AlertDialog(
            onDismissRequest = { serviceToModify = null },
            title = { Text("Modificar servei") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newRecipientName,
                        onValueChange = { newRecipientName = it },
                        label = { Text("Nom destinatari") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newRecipientAddress,
                        onValueChange = { newRecipientAddress = it },
                        label = { Text("Adreça destinatari") },
                        minLines = 2,
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newRecipientPhone,
                        onValueChange = { newRecipientPhone = it },
                        label = { Text("Telèfon destinatari") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newDimensions, onValueChange = {
                            if (it.all { char -> char.isDigit() }) {
                                newDimensions = it
                            }
                        }, label = { Text("Dimesions totals") }, trailingIcon = {
                            Text(text = "cm", style = MaterialTheme.typography.bodyMedium)
                        }, singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

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
                    Spacer(Modifier.height(8.dp))

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
                        viewModel.updateService(
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
                        serviceToModify = null
                        expandedItemId = null
                    }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { serviceToModify = null }) {
                    Text("Cancel·lar")
                }
            })
    }

    //AlertDialog per confirmar la eliminació del servei
    serviceToDelete?.let { service ->
        AlertDialog(
            onDismissRequest = { serviceToDelete = null },
            text = {
                Text("Segur que vols eliminar el servei? Aquesta acció és irreversible")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteService(service.id)
                        serviceToDelete = null
                        expandedItemId = null
                    }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { serviceToDelete = null },
                ) {
                    Text("Cancel·lar")
                }
            },
            icon = {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null)
            }
        )
    }
}