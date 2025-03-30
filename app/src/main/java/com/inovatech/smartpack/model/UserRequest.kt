package com.inovatech.smartpack.model

import com.google.gson.annotations.SerializedName

data class UserRequest(
    val email: String,
    val password: String? = null,
    val role: Role? = null,
    @SerializedName("nom") val name: String,
    @SerializedName("cognom") val surname: String,
    @SerializedName("telefon") val tel: String,
    @SerializedName("adreça") val address: String,
    @SerializedName("observacio") val observations: String? = null,
    @SerializedName("secret") val secretWord: String? = null
)