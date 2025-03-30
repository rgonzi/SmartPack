package com.inovatech.smartpack.ui.screens.user

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import androidx.compose.material3.Text

@Serializable
object MoreOptions

@Composable
fun MoreOptionsTab() {
    Text("Més opcions")
}