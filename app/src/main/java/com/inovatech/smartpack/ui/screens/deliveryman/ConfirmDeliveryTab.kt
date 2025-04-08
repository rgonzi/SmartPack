package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
object ConfirmDelivery

@Composable
fun ConfirmDeliveryTab(
) {
    Text("Aquí canviarem l'estat els serveis amb el seu codi")
}