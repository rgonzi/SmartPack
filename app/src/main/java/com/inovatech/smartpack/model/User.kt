package com.inovatech.smartpack.model

import com.inovatech.smartpack.model.api.UserRequest
import kotlinx.serialization.Serializable

/**
 * Classe que defineix la entitat Usuari.
 */
@Serializable
data class User(
    val id: Long = 0,
    val email: String? = null,
    val dni: String? = null,
    val password: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val tel: String? = null,
    val address: String? = null,
    val observations: String? = null,
    val role: Role? = null,
    val companyId: Long? = null,
)

enum class Role { ROLE_USER, ROLE_ADMIN, ROLE_DELIVERYMAN }

enum class UserFilter { ALL, USER, DELIVERYMAN }

fun User.toUserRequest() = UserRequest(
    email = email,
    password = password,
    dni = dni,
    role = role,
    name = name,
    surname = surname,
    tel = tel,
    address = address,
    observations = observations
)