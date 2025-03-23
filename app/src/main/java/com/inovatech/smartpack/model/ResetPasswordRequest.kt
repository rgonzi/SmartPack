package com.inovatech.smartpack.model

data class ResetPasswordRequest(
    val newPassword: String,
    val token: String,
)
