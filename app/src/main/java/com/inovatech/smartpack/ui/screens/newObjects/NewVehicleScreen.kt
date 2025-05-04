package com.inovatech.smartpack.ui.screens.newObjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
object NewVehicle

@Composable
fun NewVehicleScreen(
    viewModel: NewVehicleViewModel = hiltViewModel(),
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
                    onBackPressed = navigateUp,
                    title = "Crear un nou vehicle"
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
                    painter = painterResource(id = R.drawable.ic_delivery_truck),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text("Introdueix les dades del nou servei: ")

                        //Marca
                        CommonTextField(
                            value = uiState.newVehicle.brand,
                            onValueChange = {
                                viewModel.updateBrand(it)
                            },
                            label = "Marca",
                            imeAction = ImeAction.Next,
                            isError = uiState.hasTriedToCreateVehicle && uiState.newVehicle.brand.isEmpty()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        //Model
                        CommonTextField(
                            value = uiState.newVehicle.model,
                            onValueChange = {
                                viewModel.updateModel(it)
                            },
                            label = "Model",
                            imeAction = ImeAction.Next,
                            isError = uiState.hasTriedToCreateVehicle && uiState.newVehicle.model.isEmpty()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        //Matrícula
                        CommonTextField(
                            value = uiState.newVehicle.plate,
                            onValueChange = {
                                viewModel.updatePlate(it)
                            },
                            label = "Matrícula",
                            imeAction = ImeAction.Done,
                            isError = uiState.hasTriedToCreateVehicle && uiState.newVehicle.plate.isEmpty()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botó per guardar els canvis realitzats
                Button(
                    onClick = {
                        viewModel.createNewVehicle()
                    },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Crear Vehicle")
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