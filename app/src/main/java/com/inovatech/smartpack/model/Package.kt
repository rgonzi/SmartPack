package com.inovatech.smartpack.model

import com.inovatech.smartpack.model.api.PackageDTO

data class Package(
    val id: Long = 0,
    val details: String = "",
    val weight: Int = 0,
    val dimensions: String = "",
    val recipientName: String = "",
    val recipientAddress: String = "",
    val recipientPhone: String = "",
    val qrCode: String = ""
)

fun Package.toPackageDTO() = PackageDTO(
    details = details,
    weight = weight,
    dimensions = dimensions,
    recipientName = recipientName,
    recipientAddress = recipientAddress,
    recipientPhone = recipientPhone,
    qrCode = qrCode
)
