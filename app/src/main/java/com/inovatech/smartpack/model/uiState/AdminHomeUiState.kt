package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.User

/**
 * Classe que defineix l'estat de la UI de la pantalla d'inici de l'usuari administrador.
 */
data class AdminHomeUiState(
    val selectedUser: User? = null,
    val usersList: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val msg: String? = null,
) {
    val filteredUsers: List<User>
        get() = usersList.filter {
            (searchQuery.isBlank() || it.name!!.contains(searchQuery, true) || it.email!!.contains(
                searchQuery, true
            ))
        }
}
