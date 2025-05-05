package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.model.Package
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.isCompleted
import com.inovatech.smartpack.model.uiState.AdminHomeUiState
import com.inovatech.smartpack.ui.items.CommonFilterBar
import com.inovatech.smartpack.ui.items.DeleteDialog
import com.inovatech.smartpack.ui.items.ServiceListItem
import com.inovatech.smartpack.ui.screens.user.ServiceDetailsDialog
import kotlinx.serialization.Serializable

@Serializable
object ServicesTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesTab(
    viewModel: AdminHomeViewModel,
    uiState: AdminHomeUiState,
    launchSnackbar: (String) -> Unit,
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
                placeHolder = "Cerca per id, nom o telèfon destinatari"
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
                    service = service,
                    onDismiss = { viewModel.onServiceSelected(null) },
                    onUpdate = viewModel::updateService,
                    onDelete = { viewModel.deactivateService(service.id); viewModel.onServiceSelected(null) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsDialog(
    service: Service,
    onDismiss: () -> Unit,
    onUpdate: (Service) -> Unit,
    onDelete: () -> Unit,
) {
    var status by remember { mutableStateOf(service.status) }
    var isCompleted by remember { mutableStateOf(service.isCompleted()) }
    var assignedTo by remember { mutableStateOf(service.deliverymanId) }
    var recipientName by remember { mutableStateOf(service.packageToDeliver.recipientName) }
    var recipientAddress by remember { mutableStateOf(service.packageToDeliver.recipientAddress) }
    var recipientPhone by remember { mutableStateOf(service.packageToDeliver.recipientPhone) }
    var details by remember { mutableStateOf(service.packageToDeliver.details) }
    var weight by remember { mutableStateOf(service.packageToDeliver.weight) }
    var dimensions by remember { mutableStateOf(service.packageToDeliver.dimensions) }

    var showDeleteDialog by remember { mutableStateOf(false) }

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
                    minLines = 2,
                    maxLines = 2,
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
                            .weight(2f)
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

                Text(
                    "Estat: ${status}",
                    color = if (isCompleted) Color.Green else Color.Blue,
                    fontSize = 16.sp
                )
                //TODO: Mostrar Menú inferior per canviar l'estat

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Transportista assignat: #${assignedTo ?: "No"}",
                    fontSize = 16.sp
                )
                //TODO: Mostrar Menú inferior per seleccionar un nou transportista

                Spacer(modifier = Modifier.height(8.dp))

                //Botó per mostrar un dialeg per desactivar el servei
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ), onClick = { showDeleteDialog = true }, modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Eliminar servei", fontWeight = FontWeight.Bold)
                }
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
                }) { Text("Modificar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar")
            }
        })
    if (showDeleteDialog) {
        DeleteDialog(
            onDismiss = { showDeleteDialog = false },
            text = "Segur que vols eliminar aquest servei? Aquesta acció és irreversible",
            onConfirm = { onDelete(); showDeleteDialog = false })
    }
}

@Preview
@Composable
fun ServiceDetailsDialogPreview() {
    ServiceDetailsDialog(service = Service(), onDismiss = {}, onUpdate = {}, onDelete = {})
}