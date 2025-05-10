package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Role

/**
 * Classe que representa una petició per enviar un nou usuari o modificar-lo a l'API.
 */
data class UserRequest(
    val email: String = "",
    val password: String = "",
    val role: Role = Role.ROLE_USER,
    @SerializedName("dni") val dni: String = "",
    @SerializedName("nom") val name: String = "",
    @SerializedName("cognom") val surname: String = "",
    @SerializedName("telefon") val tel: String = "",
    @SerializedName("adreça") val address: String = "",
    @SerializedName("observacio") val observations: String = "",
    @SerializedName("secret") val secretWord: String = ""
)