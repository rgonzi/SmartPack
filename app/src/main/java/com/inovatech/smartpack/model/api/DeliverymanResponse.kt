package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Deliveryman
import com.inovatech.smartpack.model.Vehicle

data class DeliverymanResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("usuariId") val userId: Int,
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