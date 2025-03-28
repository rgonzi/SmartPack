package com.inovatech.smartpack.model

data class DeliveryManUiState(
    val assignedServicesList: List<String> = emptyList(), //TODO Canviar tipus de llista de serveis
    val isLogoutSuccess: Boolean = false
)
