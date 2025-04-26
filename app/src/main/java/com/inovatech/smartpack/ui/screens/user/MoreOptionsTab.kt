package com.inovatech.smartpack.ui.screens.user

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import androidx.compose.material3.Text

@Serializable
object MoreOptions

/**
 * Composable que defineix la pestanya d'altres opcions dins la pantalla d'inici de l'usuari
 */
@Composable
fun MoreOptionsTab() {
    Text("Més opcions")
}