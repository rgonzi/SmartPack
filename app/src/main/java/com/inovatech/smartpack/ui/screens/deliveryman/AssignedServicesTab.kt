package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
object AssignedServices

@Composable
fun AssignedServicesTab(
) {
    Text("Llista de serveis assignats per repartir aquell dia")
}