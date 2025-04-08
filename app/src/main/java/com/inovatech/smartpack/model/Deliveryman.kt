package com.inovatech.smartpack.model

data class Deliveryman(
    val id: Long = 0,
    val userId: Int = 0,
    val licence: String = "",
    val active: Boolean = true,
    val vehicleId: Long? = null
)
