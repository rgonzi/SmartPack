package com.inovatech.smartpack.model

import com.inovatech.smartpack.model.api.ServiceDTO

/**
 * Classe que defineix l'entitat Servei
 */
data class Service(
    val id: Long = 0,
    val status: ServiceStatus = ServiceStatus.ORDENAT,
    val userId: Long = 0,
    val deliverymanId: Long? = null,
    val packageToDeliver: Package = Package()
)

enum class ServiceStatus {
    ORDENAT, ENVIAT, TRANSIT, ENTREGAT, NO_ENTREGAT, RETORNAT
}

fun Service.toServiceDTO() = ServiceDTO(
    id = id,
    status = status,
    userId = userId,
    deliverymanId = deliverymanId,
    packageRequest = packageToDeliver.toPackageDTO()
)