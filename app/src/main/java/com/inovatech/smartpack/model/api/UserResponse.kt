package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.User

data class UserResponse(
    val id: Long = 0,
    val email: String = "",
    @SerializedName("nom") val name: String = "",
    @SerializedName("cognom") val surname: String = "",
    @SerializedName("telefon") val tel: String = "",
    @SerializedName("adreça") val address: String = "",
    @SerializedName("observacio") val observations: String = "",
)

fun UserResponse.toUser() = User(
    id = id,
    email = email,
    name = name,
    surname = surname,
    tel = tel,
    address = address,
    observations = observations
)
