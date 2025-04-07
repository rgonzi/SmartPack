package com.inovatech.smartpack.ui.screens.userConfig

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.PasswordTextField
import com.inovatech.smartpack.ui.theme.Background
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Serializable
data class ChangePassword(val id: Int)

@Composable
fun ChangePasswordScreen(
    userId: Int,
    onBackPressed: () -> Unit,
    viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
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

            LaunchedEffect(Unit) {
                viewModel.updateUserId(userId)
            }

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
                            Text("Introdueix la nova contrasenya: ")

                            PasswordTextField(
                                value = uiState.newPassword,
                                onValueChange = {
                                    viewModel.updateNewPassword(it)
                                    hasChanges = true
                                },
                                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIconClick = { viewModel.togglePasswordVisibility() },
                                imeAction = ImeAction.Next,
                                isError = !uiState.newPassword.isEmpty() && !uiState.newPassword.isValidPassword()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            //Repeat password
                            OutlinedTextField(
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                ),
                                value = uiState.repeatedNewPassword,
                                onValueChange = { viewModel.updateRepeatedNewPassword(it) },
                                label = { Text("Repeteix la contrasenya") },
                                maxLines = 1,
                                visualTransformation = PasswordVisualTransformation(),
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = "Candau"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                isError = (uiState.newPassword != uiState.repeatedNewPassword),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Botó per guardar els canvis realitzats
                    Button(
                        onClick = {
                            viewModel.changePassword()
                            hasChanges = false
                        },
                        enabled = hasChanges,
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        Text("Confirmar canvis")
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    LaunchedEffect(Unit) {
                        delay(1000)
                        scope.launch {
                            snackbarHostState.showSnackbar("No s'han pogut obtenir les dades")
                        }
                    }
                }
            }

        }
    }
    LoadingScreen(uiState.isLoading)
}