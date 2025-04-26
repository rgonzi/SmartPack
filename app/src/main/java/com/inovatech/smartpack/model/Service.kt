package com.inovatech.smartpack.model


/**
 * Classe que defineix l'entitat Servei
 */
data class Service(
    val id: Long = 0,
    val status: ServiceStatus = ServiceStatus.ORDENAT,
    val userId: Long = 0,
    val deliverymanId: Long = 0,
    val packageToDeliver: Package = Package()
)

enum class ServiceStatus {
    ORDENAT, ENVIAT, TRANSIT, ENTREGAT, NO_ENTREGAT, RETORNAT
}
