package com.inovatech.smartpack.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.*

@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    enabled: Boolean = true,
    trailingIcon: ImageVector?,
    imeAction: ImeAction,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label ?: "") },
        maxLines = 1,
        enabled = enabled,
        trailingIcon = {if (trailingIcon != null) { Icon(trailingIcon, contentDescription = "") } else null},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        isError = isError,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation,
    trailingIconClick: () -> Unit,
    imeAction: ImeAction,
    isError: Boolean
) {
    OutlinedTextField(
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        value = value,
        onValueChange = onValueChange,
        label = { Text("Contrasenya") },
        maxLines = 1,
        visualTransformation = visualTransformation,
        trailingIcon = {
            Icon(Icons.Default.Lock, contentDescription = "Candau",
                modifier = Modifier.clickable { trailingIconClick() })
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        isError = isError,
        modifier = Modifier
            .fillMaxWidth()
    )
}