package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.ServiceStatus

data class ChangeStatusRequest(
    @SerializedName("estat") val status: ServiceStatus
)
