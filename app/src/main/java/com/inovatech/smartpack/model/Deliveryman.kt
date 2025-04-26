package com.inovatech.smartpack.model

import com.inovatech.smartpack.model.api.DeliverymanRequest

/**
 * Classe que defineix la entitat Transportista.
 */
data class Deliveryman(
    val id: Long = 0,
    val userId: Long = 0,
    val licence: String = "",
    val active: Boolean = true,
    val vehicle: Vehicle = Vehicle()
)

fun Deliveryman.toDeliverymanRequest() = DeliverymanRequest(
    userId = userId,
    licence = licence
)
