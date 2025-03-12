package com.inovatech.smartpack.model

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
