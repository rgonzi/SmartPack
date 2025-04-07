package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.User

data class ChangePasswordUiState(
    val isPasswordModifiedSuccess: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val newPassword: String = "",
    val repeatedNewPassword: String = "",
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val msg: String? = null
)
