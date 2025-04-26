package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Package
import com.inovatech.smartpack.model.User

data class NewServiceUiState(
    val user: User? = null,
    val newPackage: Package = Package(),
    val hasTriedToCreateService: Boolean = false,
    val isLoading: Boolean = false,
    val newServiceCreatedSuccessfully: Boolean = false,
    val msg: String? = null
)
