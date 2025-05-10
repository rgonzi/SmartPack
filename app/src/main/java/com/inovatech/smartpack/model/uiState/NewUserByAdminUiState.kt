package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.api.UserRequest

data class NewUserByAdminUiState(
    val isLoading: Boolean = false,
    val msg: String? = null,
    val newUser: UserRequest = UserRequest()
)