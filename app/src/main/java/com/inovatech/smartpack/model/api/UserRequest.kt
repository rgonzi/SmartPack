package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Role

data class UserRequest(
    val email: String? = null,
    val password: String? = null,
    val role: Role? = null,
    @SerializedName("nom") val name: String? = null,
    @SerializedName("cognom") val surname: String? = null,
    @SerializedName("telefon") val tel: String? = null,
    @SerializedName("adreça") val address: String? = null,
    @SerializedName("observacio") val observations: String? = null,
    @SerializedName("secret") val secretWord: String? = null
)