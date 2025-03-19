package com.inovatech.smartpack.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val role: Role = Role.USER,
)