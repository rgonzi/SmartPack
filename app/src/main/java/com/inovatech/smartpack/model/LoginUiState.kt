package com.inovatech.smartpack.model

data class LoginUiState(
    var email: String = "",
    var password: String = "",
    var passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val token: String? = null
)