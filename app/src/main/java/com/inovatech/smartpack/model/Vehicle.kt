package com.inovatech.smartpack.model

import com.inovatech.smartpack.model.api.VehicleDTO

data class Vehicle(
    val id: Long = 0,
    val brand: String = "",
    val model: String = "",
    val plate: String = ""
)

fun Vehicle.toVehicleDTO(): VehicleDTO {
    return VehicleDTO(
        id = id,
        brand = brand,
        model = model,
        plate = plate
    )
}