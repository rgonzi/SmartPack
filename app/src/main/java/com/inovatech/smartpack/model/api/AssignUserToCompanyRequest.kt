package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName

/**
 * Classe que representa una petició per assignar un usuari a una empresa en concret
 */
data class AssignUserToCompanyRequest(
    @SerializedName("empresaId") val companyId: Long,
    @SerializedName("usuariId") val userId: Long,
)
