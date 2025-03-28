package com.inovatech.smartpack.model.auth

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("tokenRecovery") val tokenRecovery: String
)
