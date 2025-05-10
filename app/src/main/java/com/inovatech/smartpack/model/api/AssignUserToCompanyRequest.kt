package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

data class AssignUserToCompanyRequest(
    @SerializedName("empresaId") val companyId: Long,
    @SerializedName("usuariId") val userId: Long,
)
