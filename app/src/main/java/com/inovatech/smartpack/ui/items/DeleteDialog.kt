package com.inovatech.smartpack.ui.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit,
    text: String,
    onConfirm: () -> Unit,
) {
    AlertDialog(onDismissRequest = onDismiss, text = {
        Text(text)
    }, confirmButton = {
        TextButton(
            onClick = onConfirm
        ) {
            Text("Eliminar", color = Color.Red)
        }
    }, dismissButton = {
        TextButton(
            onClick = onDismiss,
        ) {
            Text("Cancel·lar")
        }
    }, icon = {
        Icon(imageVector = Icons.Default.Warning, contentDescription = null)
    })
}