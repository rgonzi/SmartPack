package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("tokenRecovery") val tokenRecovery: String
)
