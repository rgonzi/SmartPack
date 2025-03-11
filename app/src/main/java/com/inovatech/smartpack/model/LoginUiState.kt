package com.inovatech.smartpack.model

data class LoginUiState(
    var email: String = "",
    var password: String = "",
    var passwordVisible: Boolean = false,
    var loginTried: Boolean = false,
)