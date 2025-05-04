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
import com.inovatech.smartpack.model.Company
import com.inovatech.smartpack.model.uiState.AdminHomeUiState
import com.inovatech.smartpack.ui.items.CommonFilterBar
import com.inovatech.smartpack.ui.items.DeleteDialog
import com.inovatech.smartpack.ui.items.CompanyListItem
import kotlinx.serialization.Serializable

@Serializable
object CompanyTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyTab(
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
                "Llistat d'empreses",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            CommonFilterBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                placeHolder = "Cerca per nif, email o telèfon"
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.filteredCompanies) { company ->
                    CompanyListItem(company = company, onClick = viewModel::onCompanySelected)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            uiState.selectedCompany?.let { company ->
                CompanyDetailsDialog(
                    company = company,
                    onDismiss = { viewModel.onCompanySelected(null) },
                    onUpdate = viewModel::updateCompany,
                    onDelete = { viewModel.deactivateCompany(company.id); viewModel.onCompanySelected(null) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDetailsDialog(
    company: Company,
    onDismiss: () -> Unit,
    onUpdate: (Company) -> Unit,
    onDelete: () -> Unit,
) {
    var name by remember { mutableStateOf(company.name) }
    var email by remember { mutableStateOf(company.email) }
    var nif by remember { mutableStateOf(company.nif) }
    var tel by remember { mutableStateOf(company.phone) }
    var address by remember { mutableStateOf(company.address) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Detalls Empresa #${company.id}", fontWeight = FontWeight.Bold) },
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
                    value = nif,
                    onValueChange = { nif = it },
                    label = { Text("NIF") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = email,
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
                //TODO: Veure usuaris assignats
                Spacer(modifier = Modifier.height(8.dp))

                //Botó per mostrar un dialeg per desactivar la empresa
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ), onClick = { showDeleteDialog = true }, modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Eliminar empresa", fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(
                        company.copy(
                            name = name,
                            nif = nif,
                            email = email,
                            phone = tel,
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
            text = "Segur que vols eliminar aquesta empresa? Aquesta acció és irreversible",
            onConfirm = { onDelete(); showDeleteDialog = false }
        )
    }
}