package com.inovatech.smartpack.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable que defineix el disseny del item per a cada opció del menú de MoreOptions com a Usuari
 */
@Composable
fun MoreOptionItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ), shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun MoreOptionItemPreview() {
    MoreOptionItem(title = "OPCIÓ 1", onClick = {})
}