package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

data class DeliverymanRequest(
    @SerializedName("usuariId") val userId: Int,
    @SerializedName("llicencia") val licence: String
)
