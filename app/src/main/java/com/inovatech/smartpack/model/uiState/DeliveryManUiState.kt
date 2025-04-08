package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Deliveryman
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.Vehicle

data class DeliveryManUiState(
    val assignedServicesList: List<String> = emptyList(), //TODO Canviar tipus de llista de serveis
    val isLoading: Boolean = false,
    val hasChanges: Boolean = false,
    val msg: String? = null,
    val deliveryman: Deliveryman? = null,
    val user: User? = null,
    val vehicle: Vehicle = Vehicle()
)
