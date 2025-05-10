package com.inovatech.smartpack.ui.screens.newEntities

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.R
import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.PasswordTextField
import com.inovatech.smartpack.ui.items.BasicTopAppBar
import com.inovatech.smartpack.ui.theme.Background
import com.inovatech.smartpack.utils.isValidDni
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.serialization.Serializable

@Serializable
object NewUserByAdmin

@Composable
fun NewUserByAdminScreen(
    viewModel: NewUserByAdminViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Scaffold(
            topBar = {
                BasicTopAppBar(
                    onBackPressed = navigateUp, title = "Crear un nou Usuari"
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
                Card(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Crea un nou usuari: ")

                        //Email
                        CommonTextField(
                            value = uiState.newUser.email,
                            onValueChange = { viewModel.updateEmail(it) },
                            label = "Correu",
                            imeAction = ImeAction.Next,
                            trailingIcon = Icons.Default.Email,
                            isError = !uiState.newUser.email.isValidEmail()
                                    && uiState.newUser.email.isNotEmpty()
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Password
                        PasswordTextField(
                            value = uiState.newUser.password,
                            onValueChange = { viewModel.updatePassword(it) },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIconClick = { passwordVisible = !passwordVisible },
                            imeAction = ImeAction.Next,
                            isError = !uiState.newUser.password.isValidPassword()
                                    && uiState.newUser.password.isNotEmpty()
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Paraula secreta
                        CommonTextField(
                            value = uiState.newUser.secretWord,
                            onValueChange = { viewModel.updateSecretWord(it) },
                            label = "Paraula secreta",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //DNI
                        CommonTextField(
                            value = uiState.newUser.dni,
                            onValueChange = { viewModel.updateDni(it) },
                            label = "DNI",
                            imeAction = ImeAction.Next,
                            isError = !uiState.newUser.dni.isValidDni()
                                    && uiState.newUser.dni.isNotEmpty()
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Rol:", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))

                            RadioButton(
                                selected = uiState.newUser.role == Role.ROLE_USER,
                                onClick = { viewModel.updateRole(Role.ROLE_USER) })
                            Text("Usuari", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))

                            RadioButton(
                                selected = uiState.newUser.role == Role.ROLE_DELIVERYMAN,
                                onClick = { viewModel.updateRole(Role.ROLE_DELIVERYMAN) })
                            Text("Transportista", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))

                            RadioButton(
                                selected = uiState.newUser.role == Role.ROLE_ADMIN,
                                onClick = { viewModel.updateRole(Role.ROLE_ADMIN) })
                            Text("Admin", fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        //Name
                        CommonTextField(
                            value = uiState.newUser.name,
                            onValueChange = { viewModel.updateName(it) },
                            label = "Nom",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Surname
                        CommonTextField(
                            value = uiState.newUser.surname,
                            onValueChange = { viewModel.updateSurname(it) },
                            label = "Cognoms",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Tel number
                        CommonTextField(
                            value = uiState.newUser.tel,
                            onValueChange = { viewModel.updateTel(it) },
                            label = "Número de telèfon",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Address
                        CommonTextField(
                            value = uiState.newUser.address,
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
                    onClick = { viewModel.createUser() }, modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Crear usuari")
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
