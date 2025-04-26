package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.User

/**
 * Classe que defineix l'estat de la UI de la pantalla d'inici de l'usuari administrador.
 */
data class AdminHomeUiState(
    val user: User? = null,
    val isLogoutSuccess: Boolean = false
)
