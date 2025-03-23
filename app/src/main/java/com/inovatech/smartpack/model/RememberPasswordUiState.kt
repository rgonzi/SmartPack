package com.inovatech.smartpack.model

data class RememberPasswordUiState(
    val email: String = "",
    val newPassword: String = "",
    val passwordVisible: Boolean = false,
    val newTokenObtained: Boolean = false,
    val isLoading: Boolean = false,
    val passwordChangedSuccess: Boolean = false,
    val error: String? = null
)