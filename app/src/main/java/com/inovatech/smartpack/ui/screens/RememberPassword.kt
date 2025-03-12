package com.inovatech.smartpack.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable

@Serializable
data object RememberPassword

@Composable
fun RememberPasswordScreen(
    onNextClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    CommonSignUpScreen(
        title = "Recordar la contrasenya",
        onNextClick = onNextClick,
        onCancelClick = onCancelClick
    ) {
        Text("Introdueix")
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