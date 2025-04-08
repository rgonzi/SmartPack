package com.inovatech.smartpack.model

import com.inovatech.smartpack.model.api.PackageDTO

data class Service(
    val status: ServiceStatus = ServiceStatus.ORDENAT,
    val userId: Int = 0,
    val deliverymanId: Long = 0,
    val packageRequest: PackageDTO = PackageDTO()
)

enum class ServiceStatus {
    ORDENAT, ENVIAT, TRANSIT, ENTREGAT, NO_ENTREGAT, RETORNAT
}
