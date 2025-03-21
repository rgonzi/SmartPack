package com.inovatech.smartpack.model

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val hasTriedLogin: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null
)