package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.model.*
import com.inovatech.smartpack.model.uiState.AdminHomeUiState
import com.inovatech.smartpack.ui.items.CommonFilterBar
import com.inovatech.smartpack.ui.items.DeleteDialog
import com.inovatech.smartpack.ui.items.ServiceListItem
import kotlinx.serialization.Serializable

@Serializable
object ServicesTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesTab(
    viewModel: AdminHomeViewModel,
    uiState: AdminHomeUiState,
) {
    var showActiveServices by remember { mutableStateOf(true) }
    var showFinishedServices by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshAll() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Llistat de serveis",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            CommonFilterBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                placeHolder = "Cerca per id, nom o telèfon dest."
            )

            //Chips per filtrar serveis actuals o finalitzats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(selected = showActiveServices, onClick = {
                    showActiveServices = true
                    showFinishedServices = false
                }, label = { Text("En curs") })
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(selected = showFinishedServices, onClick = {
                    showActiveServices = false
                    showFinishedServices = true
                }, label = { Text("Finalitzats") })
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                val services = uiState.filteredServices.filter {
                    (showActiveServices && !it.isCompleted() || (showFinishedServices && it.isCompleted()))
                }

                items(services) { service ->
                    ServiceListItem(
                        service = service, onClick = { viewModel.onServiceSelected(service) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            uiState.selectedService?.let { service ->
                ServiceDetailsDialog(
                    uiState = uiState,
                    service = service,
                    onDismiss = { viewModel.onServiceSelected(null) },
                    onUpdate = viewModel::updateService,
                    onDelete = {
                        viewModel.deactivateService(service.id); viewModel.onServiceSelected(
                        null
                    )
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsDialog(
    uiState: AdminHomeUiState,
    service: Service,
    onDismiss: () -> Unit,
    onUpdate: (Service) -> Unit,
    onDelete: () -> Unit,
) {
    var status by remember { mutableStateOf(service.status) }
    var assignedTo by remember { mutableStateOf(service.deliverymanId) }
    var recipientName by remember { mutableStateOf(service.packageToDeliver.recipientName) }
    var recipientAddress by remember { mutableStateOf(service.packageToDeliver.recipientAddress) }
    var recipientPhone by remember { mutableStateOf(service.packageToDeliver.recipientPhone) }
    var details by remember { mutableStateOf(service.packageToDeliver.details) }
    var weight by remember { mutableStateOf(service.packageToDeliver.weight) }
    var dimensions by remember { mutableStateOf(service.packageToDeliver.dimensions) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showModifyDeliverymanDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Detalls Servei #${service.id}", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = recipientName,
                    onValueChange = { recipientName = it },
                    label = { Text("Nom destinatari") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = recipientPhone,
                    onValueChange = { recipientPhone = it },
                    label = { Text("Telèfon destinatari") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = recipientAddress,
                    onValueChange = { recipientAddress = it },
                    maxLines = 3,
                    label = { Text("Adreça destinatari") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = weight.toString(),
                        onValueChange = {
                            if (it.isEmpty()) {
                                weight = 1
                            } else if (it.all { char -> char.isDigit() }) {
                                weight = it.toInt()
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Pes") },
                        trailingIcon = { Text("kg") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = dimensions,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() }) {
                                dimensions = it
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        label = { Text("Mides") },
                        trailingIcon = { Text("cm") },
                        modifier = Modifier
                            .weight(1.3f)
                            .padding(vertical = 4.dp)
                    )
                }
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Detalls") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Estat del servei
                StateDropdownMenu(value = status, modifyStatus = { status = it })

                Spacer(modifier = Modifier.height(8.dp))

                //Transportista assignat
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Trans. assignat: #${assignedTo ?: "No"}", fontSize = 16.sp
                    )
                    //Botó per obrir el quadre per modificar el transportista assignat
                    Text(
                        text = "Modificar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { showModifyDeliverymanDialog = true })
                }

                Spacer(modifier = Modifier.height(8.dp))

                //Botó per mostrar un dialeg per desactivar el servei
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ), onClick = { showDeleteDialog = true }, modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Eliminar servei", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("*No oblideu confirmar els canvis!!", fontSize = 13.sp)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(
                        service.copy(
                            status = status, deliverymanId = assignedTo, packageToDeliver = Package(
                                id = service.packageToDeliver.id,
                                details = details,
                                weight = weight,
                                dimensions = dimensions,
                                recipientName = recipientName,
                                recipientAddress = recipientAddress,
                                recipientPhone = recipientPhone
                            )
                        )
                    )
                    //Tanquem el diàleg igualment
                    onDismiss()
                }) { Text("Modificar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar")
            }
        })
    //Mostrem el diàleg de confirmació per desactivar el servei
    if (showDeleteDialog) {
        DeleteDialog(
            onDismiss = { showDeleteDialog = false },
            text = "Segur que vols eliminar aquest servei? Aquesta acció és irreversible",
            onConfirm = { onDelete(); showDeleteDialog = false })
    }
    //Mostrem el diàleg per modificar el transportista dins d'un llistat donat
    if (showModifyDeliverymanDialog) {
        ModifyDeliverymanDialog(

            onDismiss = { showModifyDeliverymanDialog = false },
            onConfirm = { assignedTo = it; showModifyDeliverymanDialog = false },
            uiState = uiState,
            currentAssignedId = assignedTo
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateDropdownMenu(
    value: ServiceStatus,
    modifyStatus: (ServiceStatus) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = value.name,
            onValueChange = {},
            label = { Text("Estat") },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            ServiceStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = { Text(text = status.name) }, onClick = {
                        modifyStatus(status)
                        expanded = false
                    }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
fun ModifyDeliverymanDialog(
    uiState: AdminHomeUiState,
    currentAssignedId: Long?,
    onDismiss: () -> Unit,
    onConfirm: (Long?) -> Unit,
) {
    val deliverymenList = uiState.deliverymenList
    val selectedDeliveryman = remember {
        mutableStateOf(
            deliverymenList.find { it.id == currentAssignedId })
    }
    //Mapa que relaciona userId i nom
    val nameByUserId = remember(uiState.usersList) {
        uiState.usersList.associate { user -> user.id to user.name }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Assignar transportista", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(200.dp),
                ) {
                    item {
                        // Opció desassignar transportista"
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDeliveryman.value = null }
                        ) {
                            RadioButton(
                                selected = (selectedDeliveryman.value == null),
                                onClick = { selectedDeliveryman.value = null })
                            Text("Desassignar transportista")
                        }
                        HorizontalDivider()
                    }
                    items(deliverymenList) { deliveryman ->
                        // Transportistes disponibles

                        val userName = nameByUserId[deliveryman.userId]
                            ?: "Transportista #${deliveryman.id}"

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDeliveryman.value = deliveryman }
                        ) {
                            RadioButton(
                                selected = (selectedDeliveryman.value?.id == deliveryman.id),
                                onClick = { selectedDeliveryman.value = deliveryman })
                            Text(text = "#${deliveryman.id} - $userName")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(selectedDeliveryman.value?.id)
                    //Tanquem el diàleg igualment
                    onDismiss()
                }) { Text("Assignar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar")
            }
        }
    )
}