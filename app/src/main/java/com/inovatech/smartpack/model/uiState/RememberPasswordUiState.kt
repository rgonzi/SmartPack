package com.inovatech.smartpack.model.uiState

/**
 * Classe que defineix l'estat de la UI de la pantalla de recuperació de la contrasenya dels usuaris.
 */
data class RememberPasswordUiState(
    val email: String = "",
    val secretWord: String = "",
    val newPassword: String = "",
    val passwordVisible: Boolean = false,
    val newTokenObtained: Boolean = false,
    val isLoading: Boolean = false,
    val passwordChangedSuccess: Boolean = false,
    val error: String? = null
)