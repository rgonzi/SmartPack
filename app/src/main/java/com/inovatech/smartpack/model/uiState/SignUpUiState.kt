package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Role

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val secretWord: String = "",
    val role: Role = Role.ROLE_USER,
    val name: String = "",
    val surname: String = "",
    val tel: String = "",
    val addressType: String = "",
    val address: String = "",
    val license: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
    val hasTriedRegister: Boolean = false,
    val signUpSuccess: Boolean = false,
    val passwordVisible: Boolean = false,
)