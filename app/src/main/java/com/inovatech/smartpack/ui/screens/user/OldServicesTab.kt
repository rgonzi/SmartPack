package com.inovatech.smartpack.ui.screens.user

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
object OldServices

@Composable
fun OldServicesTab() {
    Text("Llista de serveis històrics")
}