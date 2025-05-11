package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

data class DeliveryConfirmationDTO(
    @SerializedName("telefonDestinatari") val recipientPhone: String
)
