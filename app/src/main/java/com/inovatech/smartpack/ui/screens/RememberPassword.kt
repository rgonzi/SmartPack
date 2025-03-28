package com.inovatech.smartpack.ui.screens

import ShowErrorText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.PasswordTextField
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object RememberPassword

/**
 * Composable que defineix el disseny de la pantalla de recordar la contrasenya
 *
 * @param viewModel: El seu viewModel associat
 * @param onBackClick: Destí de navegació per tornar a la pantalla Login al cancelar
 * la operació.
 */
@Composable
fun RememberPasswordScreen(
    viewModel: RememberPasswordViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    CommonInitScreen(
        title = "Has oblidat la contrasenya?",
        nomBotoPrincipal = "Següent",
        snackbarHostState = snackbarHostState,
        onNextClick = { viewModel.rememberPassword() },
        onBackClick = onBackClick
    ) {
        LaunchedEffect(uiState.passwordChangedSuccess) {
            if (uiState.passwordChangedSuccess) {
                viewModel.clearFields()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "S'ha canviat la contrasenya correctament",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

        Text("No passa res. Introdueix el teu email a continuació per poder canviar la contrasenya")
        CommonTextField(
            value = uiState.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = "Correu",
            enabled = !uiState.newTokenObtained,
            imeAction = ImeAction.Done,
            trailingIcon = Icons.Default.Email,
            isError = !uiState.email.isEmpty() && !uiState.email.isValidEmail()
        )
        //TODO Demanar la paraula secreta

        if (uiState.newTokenObtained) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Ara introdueix la nova contrasenya:")
            PasswordTextField(
                value = uiState.newPassword,
                onValueChange = { viewModel.updateNewPassword(it) },
                imeAction = ImeAction.Done,
                isError = !uiState.newPassword.isEmpty() && !uiState.newPassword.isValidPassword(),
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIconClick = viewModel::togglePasswordVisibility,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (uiState.error != null) {
            ShowErrorText(uiState.error!!)
        }
    }
    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(enabled = false) {}
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RememberPasswordPreview() {
    RememberPasswordScreen(
        onBackClick = {}
    )

}