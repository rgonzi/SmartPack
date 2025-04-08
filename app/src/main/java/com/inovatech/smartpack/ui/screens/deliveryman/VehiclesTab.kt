package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.model.uiState.DeliveryManUiState
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.LoadingScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object Vehicles

@Composable
fun VehiclesTab(
    viewModel: DeliveryManHomeViewModel,
    uiState: DeliveryManUiState,
    launchSnackbar: (String) -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading) {
            if (uiState.msg != null) {
                launchSnackbar(uiState.msg)
                viewModel.resetMsg()
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (uiState.deliveryman != null) {

            VehicleCard(
                uiState = uiState,
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botó per crear vehicle o guardar canvis
            Button(
                onClick = {
                    if (uiState.deliveryman.vehicleId == null) {
                        viewModel.createVehicle()
                    } else {
                        viewModel.saveVehicleChanges()
                    }
                    viewModel.itHasChanges(false)
                },
                enabled = uiState.hasChanges,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(if (uiState.deliveryman.vehicleId == null) "Crear vehicle" else "Guardar canvis")
            }

            Spacer(modifier = Modifier.height(32.dp))

            LicencesCard(
                uiState = uiState,
                viewModel = viewModel
            )

            if (uiState.deliveryman.vehicleId != null) {
                Spacer(modifier = Modifier.height(16.dp))

                //Botó per eliminar el vehicle
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Eliminar vehicle", fontWeight = FontWeight.Bold)
                }
            }



            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    text = {
                        Text("Segur que vols eliminar el teu vehicle? Aquesta acció és irreversible")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deactivateVehicle()
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Eliminar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteDialog = false },
                        ) {
                            Text("Cancel·lar")
                        }

                    })
            }
        } else {
            if (!uiState.isLoading) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                launchSnackbar("No s'han pogut obtenir les dades")
            }
        }
    }
}

@Composable
fun VehicleCard(
    viewModel: DeliveryManHomeViewModel,
    uiState: DeliveryManUiState,
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                "Vehicle assignat",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (uiState.deliveryman?.vehicleId == null) {
                Text("No tens cap vehicle assignat")
                Text("Pots crear-ne un ara: ")
            }
            //Marca
            CommonTextField(
                value = uiState.vehicle.brand,
                onValueChange = {
                    viewModel.updateVehicleBrand(it)
                    viewModel.itHasChanges(true)
                },
                label = "Marca",
                trailingIcon = null,
                imeAction = ImeAction.Next,
                isError = false
            )

            Spacer(modifier = Modifier.height(8.dp))
            //Model
            CommonTextField(
                value = uiState.vehicle.model,
                onValueChange = {
                    viewModel.updateVehicleModel(it)
                    viewModel.itHasChanges(true)
                },
                label = "Model",
                trailingIcon = null,
                imeAction = ImeAction.Next,
                isError = false
            )

            Spacer(modifier = Modifier.height(8.dp))
            //Matrícula
            CommonTextField(
                value = uiState.vehicle.plate,
                onValueChange = {
                    viewModel.updateVehiclePlate(it)
                    viewModel.itHasChanges(true)
                },
                label = "Matrícula",
                trailingIcon = null,
                imeAction = ImeAction.Done,
                isError = false
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun LicencesCard(
    viewModel: DeliveryManHomeViewModel,
    uiState: DeliveryManUiState,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                "Permisos de conducció",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            //Llicències
            OutlinedTextField(
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                value = uiState.deliveryman!!.licence,
                onValueChange = {
                    viewModel.updateLicences(it)
                },
                label = { Text("Permisos de conducció") },
                maxLines = 1,
                enabled = true,
                readOnly = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
