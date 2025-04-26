package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.User

/**
 * Classe que defineix l'estat de la UI de la pantalla d'inici d'un usuari
 */
data class UserHomeUiState(
    val user: User? = null,
    val services: List<Service> = emptyList(),
    val activeServices: List<Service> = emptyList(),
    val oldServices: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val msg: String? = null,
)
