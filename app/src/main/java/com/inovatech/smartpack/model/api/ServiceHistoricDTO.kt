package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.ServiceHistoric

data class ServiceHistoricDTO (
    @SerializedName("id") val id: Long,
    @SerializedName("serveId") val serviceId: Long,
    @SerializedName("transportistaId") val deliverymanId: Long,
    @SerializedName("estat") val status: String,
    @SerializedName("descripcioCanvi") val changeDescription: String,
    @SerializedName("adreçaDestinatari") val recipientAddress: String,
    @SerializedName("tipusCanvi") val statusType: String,
    @SerializedName("dataCanvi") val statusDate: String
)

fun ServiceHistoricDTO.toServiceHistoric() = ServiceHistoric(
    id = id,
    serviceId = serviceId,
    deliverymanId = deliverymanId,
    status = status,
    changeDescription = changeDescription,
    recipientAddress = recipientAddress,
    statusType = statusType,
    statusDate = statusDate
)