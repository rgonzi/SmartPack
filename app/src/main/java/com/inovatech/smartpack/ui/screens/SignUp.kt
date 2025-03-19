package com.inovatech.smartpack.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.ui.EmailTextField
import com.inovatech.smartpack.ui.PasswordTextField
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.serialization.Serializable

@Serializable
object SignUp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    goToLoginScreen: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    CommonInitScreen(
        title = "Introdueix les teves dades",
        nomBotoPrincipal = "Registrar-me",
        onNextClick = {
            viewModel.register()
            if (uiState.signUpSuccess) {
                Toast.makeText(context, "S'ha registrat correctament", Toast.LENGTH_SHORT).show()
                goToLoginScreen()
            }
        },
        onCancelClick = goToLoginScreen,
    ) {
        EmailTextField(
            value = uiState.email,
            onValueChange = viewModel::updateEmail,
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && !uiState.email.isValidEmail()
        )

        if (uiState.hasTriedRegister) {
            if (uiState.email.isEmpty()) ShowErrorText("Camp obligatori")

            if (!uiState.email.isValidEmail() && uiState.email.isNotEmpty()) {
                ShowErrorText("Introdueix un correu vàlid")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            value = uiState.password,
            onValueChange = viewModel::updatePassword,
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIconClick = { viewModel.togglePasswordVisibility() },
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && !uiState.password.isValidPassword()
        )

        if (uiState.hasTriedRegister) {
            if (uiState.password.isEmpty()) ShowErrorText("Camp obligatori")

            if (!uiState.password.isValidPassword() && uiState.password.isNotEmpty()) {
                ShowErrorText("Ha de tenir mínim 8 caràcters, almenys 1 lletra majúscula i almenys un número")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            value = uiState.repeatedPassword,
            onValueChange = viewModel::updateRepeatedPassword,
            label = { Text("Repeteix la contrasenya") },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = { Icon(Icons.Default.Lock, contentDescription = "Candau") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = uiState.hasTriedRegister && uiState.password != uiState.repeatedPassword,
            modifier = Modifier
                .fillMaxWidth()
        )

        if (uiState.hasTriedRegister && uiState.password != uiState.repeatedPassword) {
            ShowErrorText("Les contrasenyes no coincideixen")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (uiState.error != null) {
            ShowErrorText(uiState.error!!)
        }
    }
    if (uiState.isLoading) {
        //Embolcallem el CircularProgressIndicator perquè així no es pugui interectuar
        //amb la pantalla mentre s'executa el registre
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

@Composable
fun ShowErrorText(
    text: String,
) {
    Text(
        text = text,
        color = Color.Red,
        fontSize = 12.sp,
        textAlign = TextAlign.Start
    )
}

@Preview(showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}