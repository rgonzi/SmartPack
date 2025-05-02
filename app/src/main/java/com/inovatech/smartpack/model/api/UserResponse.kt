package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.User

/**
 * Classe que representa una resposta de l'API quan hem realitzat una petició sobre una entitat Usuari
 */
data class UserResponse(
    val id: Long = 0,
    val email: String = "",
    @SerializedName("nom") val name: String = "",
    @SerializedName("cognom") val surname: String = "",
    @SerializedName("telefon") val tel: String = "",
    @SerializedName("adreça") val address: String = "",
    @SerializedName("observacio") val observations: String = "",
    @SerializedName("empresaID") val companyId: Long? = null
)


//TODO Posar camp DNI

fun UserResponse.toUser() = User(
    id = id,
    email = email,
    name = name,
    surname = surname,
    tel = tel,
    address = address,
    observations = observations,
    companyId = companyId
)
