package com.inovatech.smartpack.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("tokenRecovery") val tokenRecovery: String
)
