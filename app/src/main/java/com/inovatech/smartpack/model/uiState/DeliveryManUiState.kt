package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Deliveryman
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.User

data class DeliveryManUiState(
    val assignedServices: List<Service> = emptyList(),
    val finalizedServices: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val vehicleHasChanged: Boolean = false,
    val licenseHasChanged: Boolean = false,
    val msg: String? = null,
    val deliveryman: Deliveryman? = null,
    val user: User? = null
)
