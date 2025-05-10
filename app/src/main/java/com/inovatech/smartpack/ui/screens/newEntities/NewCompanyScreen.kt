package com.inovatech.smartpack.ui.screens.newEntities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.R
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.items.BasicTopAppBar
import com.inovatech.smartpack.ui.theme.Background
import com.inovatech.smartpack.utils.isValidEmail
import kotlinx.serialization.Serializable

@Serializable
object NewCompany

@Composable
fun NewCompanyScreen(
    viewModel: NewCompanyViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
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
                    onBackPressed = navigateUp, title = "Crear una nova empresa"
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
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_business),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(120.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Crea una nova empresa: ")

                        //Email
                        CommonTextField(
                            value = uiState.newCompany.email,
                            onValueChange = { viewModel.updateEmail(it) },
                            label = "Correu",
                            imeAction = ImeAction.Next,
                            trailingIcon = Icons.Default.Email,
                            isError = !uiState.newCompany.email.isValidEmail()
                                    && uiState.newCompany.email.isNotEmpty()
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //NIF
                        CommonTextField(
                            value = uiState.newCompany.nif,
                            onValueChange = { viewModel.updateNif(it) },
                            label = "NIF",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Name
                        CommonTextField(
                            value = uiState.newCompany.name,
                            onValueChange = { viewModel.updateName(it) },
                            label = "Nom",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Tel number
                        CommonTextField(
                            value = uiState.newCompany.phone,
                            onValueChange = { viewModel.updateTel(it) },
                            label = "Número de telèfon",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Address
                        CommonTextField(
                            value = uiState.newCompany.address,
                            onValueChange = { viewModel.updateAddress(it) },
                            label = "Adreça",
                            imeAction = ImeAction.Done,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Botó per guardar els canvis realitzats
                Button(
                    onClick = { viewModel.createCompany() }, modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Crear empresa")
                }
                Spacer(modifier = Modifier.height(8.dp))

                //Botó per tornar enrere
                Button(
                    onClick = navigateUp, modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Tornar")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        LoadingScreen(uiState.isLoading)
    }
}