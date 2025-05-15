package com.inovatech.smartpack.model

import com.inovatech.smartpack.model.api.ServiceHistoricDTO

/**
 * Classe que defineix la entitat Històric de Servei.
 */
data class ServiceHistoric(
    val id: Long,
    val serviceId: Long,
    val deliverymanId: Long,
    val status: String,
    val changeDescription: String,
    val recipientAddress: String,
    val statusType: String,
    val statusDate: String,
)

fun ServiceHistoric.toServiceHistoricDTO() = ServiceHistoricDTO(
    id = this.id,
    serviceId = this.serviceId,
    deliverymanId = this.deliverymanId,
    status = this.status,
    changeDescription = this.changeDescription,
    recipientAddress = this.recipientAddress,
    statusType = this.statusType,
    statusDate = this.statusDate
)
