package com.inovatech.smartpack.model.api

data class ServiceDTO(
    val id: Long = 0,
    val status: String = "",
    val userId: Int = 0,
    val deliverymanId: Long = 0,
    val packageRequest: PackageDTO = PackageDTO()
)
