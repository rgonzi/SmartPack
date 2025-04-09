package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Vehicle

data class VehicleDTO(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("marca") val brand: String? = null,
    @SerializedName("model") val model: String? = null,
    @SerializedName("matricula") val plate: String? = null,
)

fun VehicleDTO?.toVehicle(): Vehicle {
    return Vehicle(
        id = this?.id ?: 0,
        brand = this?.brand.orEmpty(),
        model = this?.model.orEmpty(),
        plate = this?.plate.orEmpty()
    )
}