package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.User

/**
 * Classe que defineix l'estat de la UI de la pantalla de configuració de les dades d'un usuari.
 */
data class UserConfigUiState(
    val isLogoutOrDeactivateSuccess: Boolean = false,
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val msg: String? = null,
    val name: String = "",
    val surname: String = "",
    val dni: String = "",
    val tel: String = "",
    val address: String = "",
)