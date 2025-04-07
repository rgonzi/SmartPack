package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.User

data class AdminHomeUiState(
    val user: User? = null,
    val isLogoutSuccess: Boolean = false
)
