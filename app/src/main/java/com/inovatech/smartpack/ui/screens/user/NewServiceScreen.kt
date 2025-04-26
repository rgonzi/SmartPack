package com.inovatech.smartpack.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.R
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.items.BasicTopAppBar
import com.inovatech.smartpack.ui.theme.Background
import kotlinx.serialization.Serializable

@Serializable
object NewService

@Composable
fun NewServiceScreen(
    viewModel: NewServiceViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Scaffold(
            topBar = {
                BasicTopAppBar(
                    onBackPressed = navigateUp,
                    title = "Crear un nou servei"
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->

            LaunchedEffect(uiState.msg) {
                if (!uiState.isLoading && uiState.msg != null) {
                    snackbarHostState.showSnackbar(uiState.msg!!)
                    viewModel.resetMsg()
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_package_box),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text("Introdueix les dades del nou servei: ")

                        //Nom destinatari
                        CommonTextField(
                            value = uiState.newPackage.recipientName,
                            onValueChange = {
                                viewModel.updateRecipientName(it)
                            },
                            label = "Nom destinatari",
                            trailingIcon = Icons.Default.Person,
                            imeAction = ImeAction.Next,
                            isError = uiState.hasTriedToCreateService && uiState.newPackage.recipientName.isEmpty()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        //Adreça destinatari
                        CommonTextField(
                            value = uiState.newPackage.recipientAddress,
                            onValueChange = {
                                viewModel.updateRecipientAddress(it)
                            },
                            label = "Adreça destinatari",
                            trailingIcon = Icons.Default.Home,
                            imeAction = ImeAction.Next,
                            isError = uiState.hasTriedToCreateService && uiState.newPackage.recipientAddress.isEmpty()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        //Telèfon destinatari
                        OutlinedTextField(
                            value = uiState.newPackage.recipientPhone,
                            onValueChange = {
                                viewModel.updateRecipientPhone(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Telèfon destinatari") },
                            singleLine = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Phone, contentDescription = null
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            isError = uiState.hasTriedToCreateService && uiState.newPackage.recipientPhone.isEmpty()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        //Mida total del paquet (llarg + alt + amplada)
                        OutlinedTextField(
                            value = uiState.newPackage.dimensions,
                            onValueChange = {
                                if (it.all { char -> char.isDigit() }) {
                                    viewModel.updateDimensions(it)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Mides totals del paquet") },
                            singleLine = true,
                            trailingIcon = {
                                Text(text = "cm", style = MaterialTheme.typography.bodyMedium)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            isError = uiState.hasTriedToCreateService && uiState.newPackage.dimensions.isEmpty()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        //Pes del paquet
                        OutlinedTextField(
                            value = if (uiState.newPackage.weight == 0) ""
                            else uiState.newPackage.weight.toString(),
                            onValueChange = {
                                if (it.isEmpty()) {
                                    viewModel.updateWeight(0)
                                } else if (it.all { char -> char.isDigit() }) {
                                    viewModel.updateWeight(it.toInt())
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Pes del paquet") },
                            singleLine = true,
                            trailingIcon = {
                                Text(text = "kg", style = MaterialTheme.typography.bodyMedium)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            isError = uiState.hasTriedToCreateService && uiState.newPackage.weight.toString()
                                .isEmpty()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        //Detalls
                        OutlinedTextField(
                            value = uiState.newPackage.details,
                            onValueChange = {
                                viewModel.updateDetails(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Detalls") },
                            minLines = 2,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_info),
                                    contentDescription = null
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botó per guardar els canvis realitzats
                Button(
                    onClick = {
                        viewModel.createNewService()
                    },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Crear servei")
                }
                Spacer(modifier = Modifier.height(8.dp))

                //Botó per tornar enrere
                Button(
                    onClick = navigateUp,
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Tornar")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    LoadingScreen(uiState.isLoading)
}