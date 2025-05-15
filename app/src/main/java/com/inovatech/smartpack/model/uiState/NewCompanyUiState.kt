package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.api.CompanyDTO

/**
 * Classe que defineix l'estat de la UI de la pantalla de creació d'una nova empresa com a admin.
 */
data class NewCompanyUiState(
    val isLoading: Boolean = false,
    val msg: String? = null,
    val newCompany: CompanyDTO = CompanyDTO(),
)
