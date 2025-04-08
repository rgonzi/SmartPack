package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Package

data class PackageDTO(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("detalls") val details: String = "",
    @SerializedName("pes") val weight: Int = 0,
    @SerializedName("mida") val dimensions: String = "",
    @SerializedName("nomDestinatari") val recipientName: String = "",
    @SerializedName("adreçadestinatari")  val recipientAddress: String = "",
    @SerializedName("telefondestinatari")  val recipientPhone: String = "",
    @SerializedName("codiqr") val qrCode: String = ""
)

fun PackageDTO.toPackage() = Package(
    details = details,
    weight = weight,
    dimensions = dimensions,
    recipientName = recipientName,
    recipientAddress = recipientAddress,
    recipientPhone = recipientPhone,
    qrCode = qrCode
)
