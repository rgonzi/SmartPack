package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

/**
 * Classe que representa una resposta de l'API per una petició de recuperació de contrasenya.
 * Proporciona el token que utilitzarem per enviar la nova contrasenya.
 */
data class ForgotPasswordResponse(
    @SerializedName("tokenRecovery") val tokenRecovery: String
)
