package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.model.User

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val hasTriedLogin: Boolean = false,
    val loginSuccess: Boolean = false,
    val user: User? = null,
    val role: Role = Role.ROLE_USER,
    val error: String? = null
)