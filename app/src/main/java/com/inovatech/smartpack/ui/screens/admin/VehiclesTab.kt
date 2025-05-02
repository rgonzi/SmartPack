package com.inovatech.smartpack.ui.screens.admin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
object VehiclesTab

@Composable
fun VehiclesTab() {
    Text(text = "Vehicles")
}