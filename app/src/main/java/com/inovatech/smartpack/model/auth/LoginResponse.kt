package com.inovatech.smartpack.model.auth

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.Role
import java.util.Date

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("expiresIn") val expiresIn: Date,
    @SerializedName("role") val role: Role
)
