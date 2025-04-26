package com.inovatech.smartpack.model.api

/**
 * Classe que representa una petició per enviar un inici de sessió a l'API
 */
data class LoginRequest (
    val email: String,
    val password: String
)