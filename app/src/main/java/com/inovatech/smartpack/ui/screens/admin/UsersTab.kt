package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.model.User
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
    uiState: AdminHomeUiState
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshAll() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(
                "Llistat d'usuaris",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            CommonFilterBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                placeHolder = "Cerca per nom o email"
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.filteredUsers) { user ->
                    UserListItem(user = user, onClick = viewModel::onUserSelected)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            uiState.selectedUser?.let { user ->
                UserDetailsDialog(
                    user = user,
                    onDismiss = { viewModel.onUserSelected(null) },
                    onUpdate = viewModel::updateUser,
                    onDelete = { viewModel.deactivateUser(user.id); viewModel.onUserSelected(null) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsDialog(
    user: User,
    onDismiss: () -> Unit,
    onUpdate: (User) -> Unit,
    onDelete: () -> Unit,
) {
    var name by remember { mutableStateOf(user.name.orEmpty()) }
    var surname by remember { mutableStateOf(user.surname.orEmpty()) }
    var email by remember { mutableStateOf(user.email) }
    var tel by remember { mutableStateOf(user.tel.orEmpty()) }
    var address by remember { mutableStateOf(user.address.orEmpty()) }
    var company by remember { mutableStateOf(user.companyId?.toString() ?: "Sense empresa") }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                Text("Empresa assignada: #$company", textAlign = TextAlign.Start)
                Spacer(modifier = Modifier.height(8.dp))

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
        }
    )
    if (showDeleteDialog) {
        DeleteDialog(
            onDismiss = { showDeleteDialog = false },
            text = "Segur que vols eliminar aquest usuari? Aquesta acció és irreversible",
            onConfirm = { onDelete(); showDeleteDialog = false }
        )
    }
}
