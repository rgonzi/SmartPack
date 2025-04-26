package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

/**
 * Classe que representa una petició per afegir un transportista a l'API.
 */
data class DeliverymanRequest(
    @SerializedName("usuariId") val userId: Long,
    @SerializedName("llicencia") val licence: String
)
