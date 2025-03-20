package com.inovatech.smartpack.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val email: String,
    val password: String,
    val role: Role? = null,
    @SerializedName("nom") val name: String,
    @SerializedName("cognom") val surname: String,
    @SerializedName("telefon") val tel: String,
    @SerializedName("adreça") val address: String
)