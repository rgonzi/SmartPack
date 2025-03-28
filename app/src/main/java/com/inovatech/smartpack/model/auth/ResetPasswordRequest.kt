package com.inovatech.smartpack.model.auth

data class ResetPasswordRequest(
    val newPassword: String,
    val token: String,
)
