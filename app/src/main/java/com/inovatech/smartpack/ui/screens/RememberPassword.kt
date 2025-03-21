package com.inovatech.smartpack.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inovatech.smartpack.ui.CommonTextField
import com.inovatech.smartpack.utils.isValidEmail
import kotlinx.serialization.Serializable

@Serializable
data object RememberPassword

/**
 * Composable que defineix el disseny de la pantalla de recordar la contrasenya
 *
 * @param viewModel: El seu viewModel associat
 * @param onNextClick
 * @param onCancelClick: Destí de navegació per tornar a la pantalla Login al cancelar
 * la operació.
 */
@Composable
fun RememberPasswordScreen(
    viewModel: RememberPasswordViewModel = hiltViewModel(),
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
        CommonTextField(
            value = uiState.email,
            onValueChange = { viewModel::updateEmail },
            label = "Correu",
            imeAction = ImeAction.Next,
            trailingIcon = Icons.Default.Email,
            isError = !uiState.email.isEmpty() && !uiState.email.isValidEmail()
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