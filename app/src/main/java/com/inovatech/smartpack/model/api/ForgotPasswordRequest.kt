package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

/**
 * Classe que representa una petició per recuperar la contrasenya d'un usuari a l'API.
 */
data class ForgotPasswordRequest(
    val email: String,
    @SerializedName("secret") val secretWord: String
)