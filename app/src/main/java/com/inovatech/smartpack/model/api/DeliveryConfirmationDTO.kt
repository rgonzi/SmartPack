package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

/**
 * Classe que representa una petició per enviar el telèfon al confirmar la entrega d'un servei
 */
data class DeliveryConfirmationDTO(
    @SerializedName("telefonDestinatari") val recipientPhone: String
)
