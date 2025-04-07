package com.inovatech.smartpack.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.Serializable
import com.inovatech.smartpack.R
import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.PasswordTextField
import com.inovatech.smartpack.ui.theme.Background
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.coroutines.launch

@Serializable
object Login

/**
 * Composable que defineix el disseny de la pantalla de Login
 *
 * @param viewModel: El seu viewModel associat
 * @param onLoginSuccess: Destí de navegació si s'ha produït un login amb èxit
 * @param onForgotPasswordClick: Destí de navegació per anar a la pantalla de RememberPassword
 * @param onRegisterClick: Destí de navegació per anar a la pantalla de SignUp
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateUserHome: () -> Unit = {},
    onNavigateDeliveryManHome: () -> Unit = {},
    onNavigateAdminHome: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            viewModel.clearFields()
            when (uiState.role) {
                Role.ROLE_USER -> onNavigateUserHome()
                Role.ROLE_DELIVERYMAN -> onNavigateDeliveryManHome()
                Role.ROLE_ADMIN -> onNavigateAdminHome()
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(32.dp)
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        rememberCoroutineScope()

        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_transparent),
                    contentDescription = "Logo SmartPack",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Inicia sessió",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                //Email
                CommonTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = "Correu",
                    imeAction = ImeAction.Next,
                    trailingIcon = Icons.Default.Email,
                    isError = uiState.hasTriedLogin && !uiState.email.isValidEmail()
                )

                Spacer(modifier = Modifier.height(8.dp))

                PasswordTextField(
                    value = uiState.password,
                    onValueChange = viewModel::updatePassword,
                    visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIconClick = viewModel::togglePasswordVisibility,
                    imeAction = ImeAction.Done,
                    isError = uiState.hasTriedLogin && !uiState.password.isValidPassword()
                )


                Text(
                    "He oblidat la contrasenya",
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onForgotPasswordClick() },
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )

                LaunchedEffect(uiState.error) {
                    if (uiState.error != null) {
                        snackbarHostState.showSnackbar(uiState.error!!)
                        viewModel.clearError()
                    }
                }

                Spacer(modifier = Modifier.weight(0.5f))

                Button(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    enabled = !uiState.isLoading,
                    onClick = {
                        viewModel.login()
                    }
                ) {
                    Text("Iniciar sessió", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(0.6f),
                    onClick = { onRegisterClick() }
                ) {
                    Text("Registra't")
                }
                Spacer(modifier = Modifier.weight(2f))
            }
        }
    }
    LoadingScreen(uiState.isLoading)
}