package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Deliveryman

data class DeliveryManUiState(
    val assignedServicesList: List<String> = emptyList(), //TODO Canviar tipus de llista de serveis
    val isLogoutSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val msg: String? = null,
    val deliveryman: Deliveryman? = null

)
