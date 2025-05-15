package com.inovatech.smartpack.model.uiState

/**
 * Classe que defineix l'estat de la UI de la pantalla de creació d'un nou usuari com a admin.
 */
data class NewUserByAdminUiState(
    val isLoading: Boolean = false,
    val msg: String? = null
)