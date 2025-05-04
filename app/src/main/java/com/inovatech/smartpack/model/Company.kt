package com.inovatech.smartpack.model

import com.inovatech.smartpack.model.api.CompanyDTO

data class Company(
    val id: Long,
    val email: String,
    val nif: String,
    val name: String,
    val phone: String,
    val address: String,
)

fun Company.toCompanyDTO() = CompanyDTO(
    id = id,
    email = email,
    nif = nif,
    name = name,
    phone = phone,
    address = address
)

