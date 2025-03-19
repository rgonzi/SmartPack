package com.inovatech.smartpack.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("expiresIn") val expiresIn: Date
)
