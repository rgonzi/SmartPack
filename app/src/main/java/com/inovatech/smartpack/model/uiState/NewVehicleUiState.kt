package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.Vehicle

data class NewVehicleUiState(
    val user: User? = null,
    val newVehicle: Vehicle = Vehicle(),
    val hasTriedToCreateVehicle: Boolean = false,
    val isLoading: Boolean = false,
    val msg: String? = null,
)
