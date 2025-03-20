package com.inovatech.smartpack.model

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val name: String = "",
    val surname: String = "",
    val tel: String = "",
    val address: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
    val hasTriedRegister: Boolean = false,
    val signUpSuccess: Boolean = false,
    val passwordVisible: Boolean = false,
)