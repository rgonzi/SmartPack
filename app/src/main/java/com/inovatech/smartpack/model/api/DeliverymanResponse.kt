package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Deliveryman

data class DeliverymanResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("usuariId") val userId: Int,
    @SerializedName("usuariEmail") val userEmail: String,
    @SerializedName("llicencia") val licence: String
)

fun DeliverymanResponse.toDeliveryman() = Deliveryman(
    id = id,
    userId = userId,
    licence = licence,
    vehicleId = null
)