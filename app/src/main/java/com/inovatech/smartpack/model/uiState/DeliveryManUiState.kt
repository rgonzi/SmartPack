package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Deliveryman
import com.inovatech.smartpack.model.User

data class DeliveryManUiState(
    val assignedServicesList: List<String> = emptyList(), //TODO Canviar tipus de llista de serveis
    val isLoading: Boolean = false,
    val vehicleHasChanged: Boolean = false,
    val licenseHasChanged: Boolean = false,
    val msg: String? = null,
    val deliveryman: Deliveryman? = null,
    val user: User? = null
)
