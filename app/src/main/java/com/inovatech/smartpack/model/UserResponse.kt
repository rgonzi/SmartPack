package com.inovatech.smartpack.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Int = 0,
    val email: String = "",
    @SerializedName("nom") val name: String = "",
    @SerializedName("cognom") val surname: String = "",
    @SerializedName("telefon") val tel: String = "",
    @SerializedName("adreça") val address: String = "",
    val observations: String = "",
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
