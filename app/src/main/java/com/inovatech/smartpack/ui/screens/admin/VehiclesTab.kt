package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.model.Vehicle
import com.inovatech.smartpack.model.uiState.AdminHomeUiState
import com.inovatech.smartpack.ui.items.CommonFilterBar
import com.inovatech.smartpack.ui.items.DeleteDialog
import com.inovatech.smartpack.ui.items.VehicleListItem
import kotlinx.serialization.Serializable

@Serializable
object VehiclesTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiclesTab(
    viewModel: AdminHomeViewModel,
    uiState: AdminHomeUiState
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshAll() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            CommonFilterBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                placeHolder = "Cerca per matrícula"
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.filteredVehicles) { vehicle ->
                    VehicleListItem(vehicle = vehicle, onClick = viewModel::onVehicleSelected)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            uiState.selectedVehicle?.let { vehicle ->
                VehicleDetailsDialog(
                    vehicle = vehicle,
                    onDismiss = { viewModel.onVehicleSelected(null) },
                    onUpdate = viewModel::updateVehicle,
                    onDelete = {
                        viewModel.deactivateVehicle(vehicle.id); viewModel.onVehicleSelected(
                        null
                    )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailsDialog(
    vehicle: Vehicle,
    onDismiss: () -> Unit,
    onUpdate: (Vehicle) -> Unit,
    onDelete: () -> Unit,
) {
    var brand by remember { mutableStateOf(vehicle.brand) }
    var model by remember { mutableStateOf(vehicle.model) }
    var plate by remember { mutableStateOf(vehicle.plate) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Detalls Vehicle #${vehicle.id}", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marca") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Model") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    label = { Text("Matrícula") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                //Botó per mostrar un dialeg per desactivar el vehicle
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ), onClick = { showDeleteDialog = true }, modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Eliminar vehicle", fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(
                        vehicle.copy(
                            brand = brand,
                            model = model,
                            plate = plate
                        )
                    )
                }) { Text("Modificar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar")
            }
        }
    )
    if (showDeleteDialog) {
        DeleteDialog(
            onDismiss = { showDeleteDialog = false },
            text = "Segur que vols eliminar aquest vehicle? Aquesta acció és irreversible",
            onConfirm = { onDelete(); showDeleteDialog = false }
        )
    }
}
