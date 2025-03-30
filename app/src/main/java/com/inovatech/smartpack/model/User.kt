package com.inovatech.smartpack.model


data class User(
    val id: Int = 0,
    val email: String,
    val name: String = "",
    val surname: String = "",
    val tel: String = "",
    val address: String = "",
    val observations: String = "",
    val role: Role = Role.ROLE_USER,
)

enum class Role {
    ROLE_USER, ROLE_ADMIN, ROLE_DELIVERYMAN
}

fun User.toUserRequest() = UserRequest(
    email = email,
    role = role,
    name = name,
    surname = surname,
    tel = tel,
    address = address,
    observations = observations
)