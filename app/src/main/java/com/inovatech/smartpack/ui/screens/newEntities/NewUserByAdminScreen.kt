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
import com.inovatech.smartpack.model.api.UserRequest
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.PasswordTextField
import com.inovatech.smartpack.ui.items.BasicTopAppBar
import com.inovatech.smartpack.ui.screens.admin.UsersTab
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

            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var secretWord by remember { mutableStateOf("") }
            var dni by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }
            var surname by remember { mutableStateOf("") }
            var tel by remember { mutableStateOf("") }
            var address by remember { mutableStateOf("") }
            var role by remember { mutableStateOf(Role.ROLE_USER) }

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
                            value = email,
                            onValueChange = { email = it },
                            label = "Correu",
                            imeAction = ImeAction.Next,
                            trailingIcon = Icons.Default.Email,
                            isError = !email.isValidEmail()
                                    && email.isNotEmpty()
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Password
                        PasswordTextField(
                            value = password,
                            onValueChange = { password = it },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIconClick = { passwordVisible = !passwordVisible },
                            imeAction = ImeAction.Next,
                            isError = !password.isValidPassword()
                                    && password.isNotEmpty()
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Paraula secreta
                        CommonTextField(
                            value = secretWord,
                            onValueChange = { secretWord = it },
                            label = "Paraula secreta",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //DNI
                        CommonTextField(
                            value = dni,
                            onValueChange = { dni = it },
                            label = "DNI",
                            imeAction = ImeAction.Next,
                            isError = !dni.isValidDni()
                                    && dni.isNotEmpty()
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Rol:", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))

                            RadioButton(
                                selected = role == Role.ROLE_USER,
                                onClick = { role = Role.ROLE_USER })
                            Text("Usuari", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))

                            RadioButton(
                                selected = role == Role.ROLE_DELIVERYMAN,
                                onClick = { role = Role.ROLE_DELIVERYMAN })
                            Text("Transportista", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))

                            RadioButton(
                                selected = role == Role.ROLE_ADMIN,
                                onClick = { role = Role.ROLE_ADMIN })
                            Text("Admin", fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        //Name
                        CommonTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nom",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Surname
                        CommonTextField(
                            value = surname,
                            onValueChange = { surname = it },
                            label = "Cognoms",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Tel number
                        CommonTextField(
                            value = tel,
                            onValueChange = { tel = it },
                            label = "Número de telèfon",
                            imeAction = ImeAction.Next,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        //Address
                        CommonTextField(
                            value = address,
                            onValueChange = { address = it },
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
                    onClick = {
                        viewModel.createUser(
                            UserRequest(
                                email = email,
                                password = password,
                                role = role,
                                dni = dni,
                                name = name,
                                surname = surname,
                                tel = tel,
                                address = address,
                                secretWord = secretWord
                            )
                        )
                    }, modifier = Modifier.fillMaxWidth(0.6f)
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
