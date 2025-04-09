package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Deliveryman

data class DeliverymanResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("usuariId") val userId: Long,
    @SerializedName("usuariEmail") val userEmail: String,
    @SerializedName("llicencia") val licence: String,
    @SerializedName("vehicle") val vehicle: VehicleDTO?
)

fun DeliverymanResponse.toDeliveryman() = Deliveryman(
    id = id,
    userId = userId,
    licence = licence,
    vehicle = vehicle.toVehicle()
)