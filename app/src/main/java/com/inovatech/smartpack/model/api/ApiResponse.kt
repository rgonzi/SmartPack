package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

/**
 * Classe de tipus Response per deserialitzar el missatge d'error/confirmació de la API
 */
data class ApiResponse(
    @SerializedName("message") val msg: String? = null
)
