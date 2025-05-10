package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Company

data class CompanyDTO(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("email") val email: String = "",
    @SerializedName("nif") val nif: String = "",
    @SerializedName("nom") val name: String = "",
    @SerializedName("telefon") val phone: String = "",
    @SerializedName("adreça") val address: String = ""
)

fun CompanyDTO.toCompany() = Company(
    id = id,
    email = email,
    nif = nif,
    name = name,
    phone = phone,
    address = address
)