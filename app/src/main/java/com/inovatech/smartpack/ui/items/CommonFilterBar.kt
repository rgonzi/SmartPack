package com.inovatech.smartpack.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun CommonFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeHolder: String
) {
    //Quadre de cerca
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        Icons.Filled.Clear, contentDescription = null
                    )
                }
            }
        },
        placeholder = { Text(placeHolder) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
        )
    )
}