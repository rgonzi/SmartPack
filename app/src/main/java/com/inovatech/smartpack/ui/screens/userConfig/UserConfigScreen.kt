package com.inovatech.smartpack.ui.screens.userConfig


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.theme.Background
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object UserConfig

@Composable
fun UserConfigScreen(
    viewModel: UserConfigViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    backToLogin: () -> Unit,
    onChangePassword: (Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var hasChanges by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Scaffold(
            topBar = { UserConfigTopAppBar(onBackPressed = onBackPressed) },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->

            LaunchedEffect(uiState.msg) {
                if (!uiState.isLoading) {
                    if (uiState.msg != null) {
                        snackbarHostState.showSnackbar(uiState.msg!!)
                        viewModel.resetMsg()
                    }
                }
            }
            LaunchedEffect(uiState.errorMessage) {
                if (!uiState.isLoading) {
                    if (uiState.errorMessage != null) {
                        snackbarHostState.showSnackbar(uiState.errorMessage!!)
                        viewModel.resetErrorMessage()
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                if (uiState.user != null) {

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {


                            Text("Edita les dades que vulguis: ")

                            //Name
                            CommonTextField(
                                value = uiState.user!!.name ?: "",
                                onValueChange = {
                                    viewModel.updateName(it)
                                    hasChanges = true
                                },
                                label = "Nom",
                                trailingIcon = Icons.Default.Person,
                                imeAction = ImeAction.Done,
                                isError = false
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            //Surname
                            CommonTextField(
                                value = uiState.user!!.surname ?: "",
                                onValueChange = {
                                    viewModel.updateSurname(it)
                                    hasChanges = true
                                },
                                label = "Cognoms",
                                trailingIcon = Icons.Default.Person,
                                imeAction = ImeAction.Done,
                                isError = false
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            //Tel number
                            CommonTextField(
                                value = uiState.user!!.tel ?: "",
                                onValueChange = {
                                    viewModel.updateTel(it)
                                    hasChanges = true
                                },
                                label = "Número de telèfon",
                                trailingIcon = Icons.Default.Phone,
                                imeAction = ImeAction.Done,
                                isError = false
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            //Address
                            CommonTextField(
                                value = uiState.user!!.address ?: "",
                                onValueChange = {
                                    viewModel.updateAddress(it)
                                    hasChanges = true
                                },
                                label = "Adreça",
                                trailingIcon = Icons.Default.Home,
                                imeAction = ImeAction.Done,
                                isError = false
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            //Observations
                            OutlinedTextField(
                                value = uiState.user!!.observations ?: "",
                                onValueChange = {
                                    viewModel.updateObservations(it)
                                    hasChanges = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Observacions") },
                                minLines = 2,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Canviar la contrasenya",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .clickable { onChangePassword(uiState.user!!.id) },
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // Botó per guardar els canvis realitzats
                    Button(
                        onClick = {
                            viewModel.saveChanges()
                            hasChanges = false
                        },
                        enabled = hasChanges,
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        Text("Confirmar canvis")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    //Botó per tancar sessió
                    Button(
                        onClick = {
                            viewModel.logout()
                            backToLogin()
                        },
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        Text("Tancar sessió")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //Botó per eliminar el compte
                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Eliminar compte", fontWeight = FontWeight.Bold)
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
                        LaunchedEffect(Unit) {
                            scope.launch {
                                snackbarHostState.showSnackbar("No s'han pogut obtenir les dades")
                            }
                        }
                    }
                }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        text = {
                            Text("Segur que vols eliminar el teu compte? Aquesta acció és irreversible")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.deactivateAccount()
                                    showDeleteDialog = false
                                    backToLogin()
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
            }
        }
        LoadingScreen(uiState.isLoading)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserConfigTopAppBar(
    onBackPressed: () -> Unit,
) {
    TopAppBar(
        title = { Text("Configuració de l'usuari") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton(
                onClick = { onBackPressed() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = ""
                )
            }
        }
    )
}