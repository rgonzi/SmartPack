package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.ServiceStatus

/**
 * Classe que representa una petició i una resposta per realitzar accions sobre l'entitat Servei
 */
data class ServiceDTO(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("estat") val status: ServiceStatus? = null,
    @SerializedName("usuariId") val userId: Long? = null,
    @SerializedName("transportistaId") val deliverymanId: Long? = null,
    @SerializedName("paquet") val packageRequest: PackageDTO? = PackageDTO()
)

fun ServiceDTO?.toService() = Service(
    id = this?.id ?: 0,
    status = this?.status ?: ServiceStatus.ORDENAT,
    userId = this?.userId ?: 0,
    deliverymanId = this?.deliverymanId,
    packageToDeliver = this?.packageRequest.toPackage()
)
