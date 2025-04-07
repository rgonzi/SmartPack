package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("message") val msg: String? = null
)
