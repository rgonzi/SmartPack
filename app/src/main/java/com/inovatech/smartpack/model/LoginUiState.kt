package com.inovatech.smartpack.model

import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val hasTriedLogin: Boolean = false,
    val error: String? = null,
    val token: String? = null
)