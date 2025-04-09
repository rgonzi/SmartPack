package com.inovatech.smartpack.model.api

import com.inovatech.smartpack.model.Service

data class ServiceDTO(
    val id: Long = 0,
    val status: String = "",
    val userId: Int = 0,
    val deliverymanId: Long = 0,
    val packageRequest: PackageDTO = PackageDTO()
)

fun ServiceDTO.toService() = Service(
    id = id,
    status = status,
    userId = userId,
    deliverymanId = deliverymanId,
    packageToDeliver = packageRequest.toPackage()
)
