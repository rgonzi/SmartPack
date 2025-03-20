package com.inovatech.smartpack.model

import java.util.Date

data class Usuari(
    val id: Int? = null,
    val email: String,
    val password: String,
    val role: Role = Role.ROLE_USER,
    val enabled: Boolean? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

enum class Role {
    ROLE_USER, ROLE_ADMIN, ROLE_DELIVERYMAN
}