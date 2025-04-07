package com.inovatech.smartpack.model.api

data class ResetPasswordRequest(
    val newPassword: String,
    val token: String,
)
