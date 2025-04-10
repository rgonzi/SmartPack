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

fun PackageDTO?.toPackage() = Package(
    details = this?.details.orEmpty(),
    weight = this?.weight ?: 0,
    dimensions = this?.dimensions.orEmpty(),
    recipientName = this?.recipientName.orEmpty(),
    recipientAddress = this?.recipientAddress.orEmpty(),
    recipientPhone = this?.recipientPhone.orEmpty(),
    qrCode = this?.qrCode.orEmpty()
)
