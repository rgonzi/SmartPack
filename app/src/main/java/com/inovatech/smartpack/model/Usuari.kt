package com.inovatech.smartpack.model

import java.util.Date

data class Usuari(
    val id: Int? = null,
    val email: String,
    val password: String,
    val role: Role? = null,
    val enabled: Boolean? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

enum class Role {
    ADMIN, USER
}