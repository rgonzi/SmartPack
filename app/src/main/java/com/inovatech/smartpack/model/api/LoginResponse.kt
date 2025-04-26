package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Role
import java.util.Date

/**
 * Classe que representa una resposta després de realitzar un inici de sessió exitosament.
 */
data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("expiresIn") val expiresIn: Date,
    @SerializedName("role") val role: Role
)
