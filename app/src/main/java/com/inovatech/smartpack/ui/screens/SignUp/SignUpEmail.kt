package com.inovatech.smartpack.ui.screens.SignUp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
object SignUpEmail

@Composable
fun SignUpEmailScreen() {
    CommonSignUpScreen(
        onNextClick = { /*TODO: Navegar a la pantalla de dades personals*/ },
        onCancelClick = { /*TODO: Tornar a la pantalla de login*/ },
    ) {
        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            value = "",
            onValueChange = {},
            label = { Text("Correu") },
            trailingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            value = "",
            onValueChange = {},
            label = { Text("Contrasenya") },
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = { Icon(Icons.Default.Lock, contentDescription = "Candau") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            value = "",
            onValueChange = {},
            label = { Text("Repeteix la contrasenya") },
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = { Icon(Icons.Default.Lock, contentDescription = "Candau") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
    }
}





@Preview(showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpEmailScreen()
}