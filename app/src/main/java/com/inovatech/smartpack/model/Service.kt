package com.inovatech.smartpack.model


data class Service(
    val id: Long = 0,
    val status: ServiceStatus = ServiceStatus.ORDENAT,
    val userId: Int = 0,
    val deliverymanId: Long = 0,
    val packageToDeliver: Package = Package()
)

enum class ServiceStatus {
    ORDENAT, ENVIAT, TRANSIT, ENTREGAT, NO_ENTREGAT, RETORNAT
}
