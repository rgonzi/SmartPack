package com.inovatech.smartpack.ui.screens

import ShowErrorText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.ui.LoadingScreen
import com.inovatech.smartpack.ui.PasswordTextField
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.coroutines.launch
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
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var checked by remember { mutableStateOf(false) }

    CommonInitScreen(
        title = "Introdueix les teves dades",
        nomBotoPrincipal = "Registrar-me",
        snackbarHostState = snackbarHostState,
        onNextClick = { viewModel.register() },
        onBackClick = goToLoginScreen,
    ) {
        LaunchedEffect(uiState.signUpSuccess) {
            if (uiState.signUpSuccess) {
                viewModel.clearFields()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "S'ha registrat correctament",
                        duration = SnackbarDuration.Short
                    )
                }
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

        Spacer(modifier = Modifier.height(4.dp))

        PasswordTextField(
            value = uiState.password,
            onValueChange = { viewModel.updateField("password", it) },
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIconClick = { viewModel.togglePasswordVisibility() },
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && !uiState.password.isValidPassword()
        )

        Spacer(modifier = Modifier.height(4.dp))

        //Repeat password
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

        Spacer(modifier = Modifier.height(4.dp))

        //Paraula secreta
        Text(
            "Paraula secreta en cas de recuperar la contrasenya",
            fontSize = 14.sp
        )
        CommonTextField(
            value = uiState.secretWord,
            onValueChange = { viewModel.updateField("secretWord", it) },
            label = "Paraula secreta",
            trailingIcon = Icons.Default.Warning,
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && uiState.secretWord.isEmpty(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Transportista?", fontSize = 12.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                thumbContent = {
                    if (checked) {
                        viewModel.updateRole(Role.ROLE_DELIVERYMAN)
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "",
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    }
                }
            )
            if (checked) {
                CommonTextField(
                    value = uiState.license,
                    onValueChange = { viewModel.updateField("license", it) },
                    label = "Permís de conduïr",
                    trailingIcon = null,
                    imeAction = ImeAction.Next,
                    isError = uiState.hasTriedRegister && uiState.license.isEmpty()
                )
            }
        }

        //Name
        CommonTextField(
            value = uiState.name,
            onValueChange = { viewModel.updateField("name", it) },
            label = "Nom",
            trailingIcon = Icons.Default.Person,
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && uiState.name.isEmpty()
        )

        Spacer(modifier = Modifier.height(4.dp))

        //Surname
        CommonTextField(
            value = uiState.surname,
            onValueChange = { viewModel.updateField("surname", it) },
            label = "Cognoms",
            trailingIcon = Icons.Default.Person,
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && uiState.surname.isEmpty()
        )
        Spacer(modifier = Modifier.height(4.dp))

        //Tel number
        CommonTextField(
            value = uiState.tel,
            onValueChange = { viewModel.updateField("tel", it) },
            label = "Número de telèfon",
            trailingIcon = Icons.Default.Phone,
            imeAction = ImeAction.Next,
            isError = uiState.hasTriedRegister && uiState.tel.isEmpty()
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            CommonTextField(
                value = uiState.addressType,
                onValueChange = { viewModel.updateField("addressType", it) },
                label = "Via",
                trailingIcon = Icons.Default.Place,
                imeAction = ImeAction.Next,
                isError = uiState.hasTriedRegister && uiState.addressType.isEmpty(),
                modifier = Modifier.fillMaxWidth(0.3f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            //Address
            CommonTextField(
                value = uiState.address,
                onValueChange = { viewModel.updateField("address", it) },
                label = "Adreça",
                trailingIcon = Icons.Default.Home,
                imeAction = ImeAction.Done,
                isError = uiState.hasTriedRegister && uiState.address.isEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        SideEffect {
            if (!uiState.isLoading) {
                if (uiState.error != null) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message = uiState.error!!)
                    }
                }
            }
        }
    }
    LoadingScreen(uiState.isLoading)
}