package com.inovatech.smartpack.model.api

/**
 * Classe per enviar una petició de recuperació de contrasenya a l'API amb el token que ens ha
 * proporcionat prèviament
 */
data class ResetPasswordRequest(
    val newPassword: String,
    val token: String,
)
