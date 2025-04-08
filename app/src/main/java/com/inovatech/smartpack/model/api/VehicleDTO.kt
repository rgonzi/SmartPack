package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Vehicle

data class VehicleDTO(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("marca") val brand: String = "",
    @SerializedName("model") val model: String = "",
    @SerializedName("matricula") val plate: String = "",
)

fun VehicleDTO.toVehicle() = Vehicle(
    id = id,
    brand = brand,
    model = model,
    plate = plate
)