package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.User

data class UserHomeUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val msg: String? = null,
)
