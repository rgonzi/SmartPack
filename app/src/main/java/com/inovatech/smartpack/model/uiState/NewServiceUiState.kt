package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Package
import com.inovatech.smartpack.model.User

/**
 * Classe que defineix l'estat de la UI de la pantalla de creació d'un nou servei com a admin.
 */
data class NewServiceUiState(
    val user: User? = null,
    val newPackage: Package = Package(),
    val hasTriedToCreateService: Boolean = false,
    val isLoading: Boolean = false,
    val msg: String? = null
)
