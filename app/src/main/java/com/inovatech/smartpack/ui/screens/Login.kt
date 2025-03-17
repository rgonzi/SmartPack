package com.inovatech.smartpack.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.Serializable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.inovatech.smartpack.R
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.ui.EmailTextField
import com.inovatech.smartpack.ui.PasswordTextField
import com.inovatech.smartpack.ui.theme.Background
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword

@Serializable
object Login

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.logo_transparent),
                contentDescription = "Logo SmartPack",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Inicia sessió",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                //Email
                EmailTextField(
                    value = uiState.email,
                    onValueChange = viewModel::updateEmail,
                    imeAction = ImeAction.Next,
                    isError = uiState.hasTriedLogin && !uiState.email.isValidEmail()
                )

                if (uiState.hasTriedLogin) {
                    if (uiState.email.isEmpty()) ShowErrorText("Camp obligatori")

                    if (!uiState.email.isValidEmail() && uiState.email.isNotEmpty()) {
                        ShowErrorText("Introdueix un correu vàlid")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                //Password
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

                if (uiState.hasTriedLogin && uiState.password.isEmpty()) {
                    ShowErrorText("Camp obligatori")
                }

                Spacer(modifier = Modifier.weight(0.5f))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.login() }
                ) {
                    Text("Iniciar sessió", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onRegisterClick() }
                ) {
                    Text("Registra't")
                }
                Spacer(modifier = Modifier.weight(2f))
            }
        }
        if (uiState.isLoading) {
            /* Embolcallem el CircularProgressIndicator perquè així no es pugui
            interectuar amb la pantalla mentre s'executa el login */
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)) // Fons semitransparent
                    .clickable(enabled = false) {} // Evita interaccions
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}