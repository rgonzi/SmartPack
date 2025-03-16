package com.inovatech.smartpack.model

data class Usuari(
    val id: Int? = null,
    val email: String,
    val password: String,
    val role: String = "USER",
    val enabled: Boolean
    //TODO Acabar de posar tots els camps de la BBDD per a un usuari
)
