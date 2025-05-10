package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.model.Deliveryman
import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.UserFilter
import com.inovatech.smartpack.model.Vehicle
import com.inovatech.smartpack.model.uiState.AdminHomeUiState
import com.inovatech.smartpack.ui.items.CommonFilterBar
import com.inovatech.smartpack.ui.items.DeleteDialog
import com.inovatech.smartpack.ui.items.UserListItem
import kotlinx.serialization.Serializable

@Serializable
object UsersTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersTab(
    viewModel: AdminHomeViewModel,
    uiState: AdminHomeUiState,
) {
    var userFilter by remember { mutableStateOf(UserFilter.ALL) }
    val usersDisplayed = uiState.filteredUsers(uiState.searchQuery, userFilter)

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshAll() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            CommonFilterBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                placeHolder = "Cerca per nom o email"
            )
            //Filtre per mostrar només usuaris normals o transportistes
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = userFilter == UserFilter.ALL,
                    onClick = { userFilter = UserFilter.ALL },
                    label = { Text("Tots") })
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = userFilter == UserFilter.USER,
                    onClick = { userFilter = UserFilter.USER },
                    label = { Text("Usuaris") })
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = userFilter == UserFilter.DELIVERYMAN,
                    onClick = { userFilter = UserFilter.DELIVERYMAN },
                    label = { Text("Transportistes") })
            }

            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(usersDisplayed) { user ->
                    UserListItem(user = user, onClick = viewModel::onUserSelected)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            uiState.selectedUser?.let { user ->
                val deliveryman = uiState.deliverymenList.find { it.userId == user.id }
                UserDetailsDialog(
                    uiState = uiState,
                    user = user,
                    deliveryman = deliveryman,
                    onDismiss = { viewModel.onUserSelected(null) },
                    onUpdate = viewModel::updateUser,
                    onAssignedCompanyChange = { companyId ->
                        companyId
                            ?.let { viewModel.assignCompany(user.id, it) }
                            ?: viewModel.deassignCompany(user.id)
                    },
                    onAssignedVehicleChange = { vehicle ->
                        vehicle
                            ?.let { viewModel.assignVehicle(deliveryman!!.id, it.id); }
                            ?: viewModel.deassignVehicle(deliveryman!!.id)
                    },
                    onDelete = {
                        viewModel.deactivateUser(user.id)
                        viewModel.onUserSelected(null)
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsDialog(
    uiState: AdminHomeUiState,
    user: User,
    deliveryman: Deliveryman? = null,
    onDismiss: () -> Unit,
    onUpdate: (User) -> Unit,
    onAssignedCompanyChange: (Long?) -> Unit,
    onAssignedVehicleChange: (Vehicle?) -> Unit,
    onDelete: () -> Unit,
) {
    var name by remember { mutableStateOf(user.name.orEmpty()) }
    var surname by remember { mutableStateOf(user.surname.orEmpty()) }
    var email by remember { mutableStateOf(user.email) }
    var tel by remember { mutableStateOf(user.tel.orEmpty()) }
    var address by remember { mutableStateOf(user.address.orEmpty()) }

    var assignedCompanyId by remember(user) { mutableStateOf(user.companyId) }
    var assignedVehicle by remember(deliveryman) { mutableStateOf(deliveryman?.vehicle) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAssignVehicleDialog by remember { mutableStateOf(false) }
    var showAssignCompanyDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Detalls Usuari #${user.id}", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Cognoms") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = email!!,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = tel,
                    onValueChange = { tel = it },
                    label = { Text("Telèfon") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Adreça") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Empresa assignada: #${assignedCompanyId ?: "No"}",
                        textAlign = TextAlign.Start
                    )
                    //Botó per obrir el quadre per modificar la empresa assignada
                    Text(
                        text = "Modificar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { showAssignCompanyDialog = true }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (user.role == Role.ROLE_DELIVERYMAN) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Vehicle assignat: #${if (assignedVehicle?.id != 0L) assignedVehicle?.id ?: "No" else "No"}",
                            textAlign = TextAlign.Start
                        )
                        //Botó per obrir el quadre per modificar el vehicle assignat
                        Text(
                            text = "Modificar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { showAssignVehicleDialog = true }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                //Botó per mostrar un dialeg per desactivar l'usuari
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ), onClick = { showDeleteDialog = true }, modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Eliminar usuari", fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(
                        user.copy(
                            name = name,
                            surname = surname,
                            email = email,
                            tel = tel,
                            address = address
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
            text = "Segur que vols eliminar aquest usuari? Aquesta acció és irreversible",
            onConfirm = { onDelete(); showDeleteDialog = false })
    }

    //Mostrem el diàleg per assignar/desassignar una empresa a un usuari
    if (showAssignCompanyDialog) {
        AssignCompanyToUserDialog(
            onDismiss = { showAssignCompanyDialog = false },
            onConfirm = {
                assignedCompanyId = it
                onAssignedCompanyChange(it)
                showAssignCompanyDialog = false
            },
            uiState = uiState,
            currentAssigned = assignedCompanyId
        )
    }

    //Mostrem el diàleg per assignar/desassignar un vehicle a un transportista
    if (showAssignVehicleDialog && user.role == Role.ROLE_DELIVERYMAN) {
        AssignVehicleToDeliverymanDialog(
            onDismiss = { showAssignVehicleDialog = false },
            onConfirm = {
                assignedVehicle = it
                onAssignedVehicleChange(it)
                showAssignVehicleDialog = false
            },
            uiState = uiState,
            currentAssigned = assignedVehicle
        )
    }
}

@Composable
fun AssignCompanyToUserDialog(
    uiState: AdminHomeUiState,
    currentAssigned: Long?,
    onDismiss: () -> Unit,
    onConfirm: (Long?) -> Unit,
) {
    val companyList = uiState.companiesList
    var selectedCompany = remember {
        mutableStateOf(companyList.find { it.id == currentAssigned }?.id)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Assignar empresa", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(200.dp),
                ) {
                    item {
                        // Opció desassignar empresa
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedCompany.value = null }) {
                            RadioButton(
                                selected = (selectedCompany.value == null),
                                onClick = { selectedCompany.value = null })
                            Text("Desassignar empresa")
                        }
                        HorizontalDivider()
                    }
                    items(companyList) { company ->
                        // Empreses disponibles
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedCompany.value = company.id }
                        ) {
                            RadioButton(
                                selected = (selectedCompany.value == company.id),
                                onClick = { selectedCompany.value = company.id }
                            )
                            Text(text = "#${company.id} - ${company.name} ${company.nif}")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(selectedCompany.value)
                    //Tanquem el diàleg igualment
                    onDismiss()
                }) { Text("Assignar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar")
            }
        })
}

@Composable
fun AssignVehicleToDeliverymanDialog(
    uiState: AdminHomeUiState,
    currentAssigned: Vehicle?,
    onDismiss: () -> Unit,
    onConfirm: (Vehicle?) -> Unit,
) {
    val vehicleList = uiState.vehiclesList
    var selectedVehicle = remember {
        mutableStateOf(vehicleList.find { it.id == currentAssigned?.id })
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Assignar vehicle", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(200.dp),
                ) {
                    item {
                        // Opció desassignar vehicle"
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedVehicle.value = null }) {
                            RadioButton(
                                selected = (selectedVehicle.value == null),
                                onClick = { selectedVehicle.value = null })
                            Text("Desassignar vehicle")
                        }
                        HorizontalDivider()
                    }
                    items(vehicleList) { vehicle ->
                        // Vehicles disponibles
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedVehicle.value = vehicle }
                        ) {
                            RadioButton(
                                selected = (selectedVehicle.value?.id == vehicle.id),
                                onClick = { selectedVehicle.value = vehicle }
                            )
                            Text(text = "#${vehicle.id} - ${vehicle.brand} ${vehicle.model}")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(selectedVehicle.value)
                    //Tanquem el diàleg igualment
                    onDismiss()
                }) { Text("Assignar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar")
            }
        })
}