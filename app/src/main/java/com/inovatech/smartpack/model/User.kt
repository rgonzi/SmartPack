package com.inovatech.smartpack.model


data class User(
    val id: Int = 0,
    val email: String? = null,
    val password: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val tel: String? = null,
    val address: String? = null,
    val observations: String? = null,
    val role: Role = Role.ROLE_USER,
)

enum class Role {
    ROLE_USER, ROLE_ADMIN, ROLE_DELIVERYMAN
}

fun User.toUserRequest() = UserRequest(
    email = email,
    password = password,
    role = role,
    name = name,
    surname = surname,
    tel = tel,
    address = address,
    observations = observations
)