package com.inovatech.smartpack.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.PasswordTextField
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.serialization.Serializable

@Serializable
object SignUp

/**
 * Composable que defineix el disseny de la pantalla de Login
 *
 * @param viewModel: El seu viewModel associat
 * @param goToLoginScreen: Destí de navegació per anar a la pantalla de Login un
 * cop s'ha registrat correctament o es vol cancelar la operació
 */
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
        onNextClick = { viewModel.register() },
        onCancelClick = goToLoginScreen,
    ) {
        LaunchedEffect(uiState.signUpSuccess) {
            if (uiState.signUpSuccess) {
                viewModel.clearFields()
                Toast.makeText(context, "S'ha registrat correctament", Toast.LENGTH_SHORT).show()
                goToLoginScreen()
            }
        }
        //Email
        CommonTextField(
            value = uiState.email,
            onValueChange = { viewModel.updateField("email", it) },
            label = "Correu",
            imeAction = ImeAction.Next,
            trailingIcon = Icons.Default.Email,
            isError = uiState.hasTriedRegister && !uiState.email.isValidEmail()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            value = uiState.password,
            onValueChange = { viewModel.updateField("password", it) },
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIconClick = { viewModel.togglePasswordVisibility() },
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && !uiState.password.isValidPassword()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            value = uiState.repeatedPassword,
            onValueChange = { viewModel.updateField("repeatedPassword", it) },
            label = { Text("Repeteix la contrasenya") },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = { Icon(Icons.Default.Lock, contentDescription = "Candau") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            isError = uiState.hasTriedRegister &&
                    (uiState.password != uiState.repeatedPassword || uiState.repeatedPassword.isEmpty()),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Name
        CommonTextField(
            value = uiState.name,
            onValueChange = { viewModel.updateField("name", it) },
            label = "Nom",
            trailingIcon = Icons.Default.Person,
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && uiState.name.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Surname
        CommonTextField(
            value = uiState.surname,
            onValueChange = { viewModel.updateField("surname", it) },
            label = "Cognoms",
            trailingIcon = Icons.Default.Person,
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && uiState.surname.isEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Tel number
        CommonTextField(
            value = uiState.tel,
            onValueChange = { viewModel.updateField("tel", it) },
            label = "Número de telèfon",
            trailingIcon = Icons.Default.Phone,
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && uiState.tel.isEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Address
        CommonTextField(
            value = uiState.address,
            onValueChange = { viewModel.updateField("address", it) },
            label = "Adreça",
            trailingIcon = Icons.Default.Place,
            imeAction = ImeAction.Done,
            isError = uiState.hasTriedRegister && uiState.address.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

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