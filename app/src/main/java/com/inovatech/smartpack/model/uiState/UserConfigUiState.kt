package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.User

data class UserConfigUiState(
    val isLogoutOrDeactivateSuccess: Boolean = false,
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val msg: String? = null,
    val name: String = "",
    val surname: String = "",
    val tel: String = "",
    val address: String = "",
)