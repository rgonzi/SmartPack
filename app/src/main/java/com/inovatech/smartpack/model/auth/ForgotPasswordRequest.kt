package com.inovatech.smartpack.model.auth

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(
    val email: String,
    @SerializedName("secret") val secretWord: String
)