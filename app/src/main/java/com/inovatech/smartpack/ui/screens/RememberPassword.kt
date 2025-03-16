package com.inovatech.smartpack.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable

@Serializable
data object RememberPassword

@Composable
fun RememberPasswordScreen(
    viewModel: RememberPasswordViewModel = viewModel(),
    onNextClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    CommonInitScreen(
        title = "Has oblidat la contrasenya?",
        nomBotoPrincipal = "Enviar",
        onNextClick = onNextClick,
        onCancelClick = onCancelClick
    ) {
        Text("No passa res. Introdueix el teu email a continació per poder cambiar la contrasenya")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            value = uiState.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Correu") },
            maxLines = 1,
            trailingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun RememberPasswordPreview() {
    RememberPasswordScreen(
        onNextClick = {},
        onCancelClick = {}
    )

}