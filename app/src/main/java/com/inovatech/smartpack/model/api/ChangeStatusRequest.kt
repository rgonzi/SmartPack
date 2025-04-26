package com.inovatech.smartpack.model.api

import com.google.gson.annotations.SerializedName
import com.inovatech.smartpack.model.ServiceStatus

/**
 * Classe que representa una petició per canviar l'estat d'un servei des de l'API.
 */
data class ChangeStatusRequest(
    @SerializedName("estat") val status: ServiceStatus
)
