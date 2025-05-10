package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.api.CompanyDTO
import com.inovatech.smartpack.model.api.UserRequest

data class NewCompanyUiState(
    val isLoading: Boolean = false,
    val msg: String? = null,
    val newCompany: CompanyDTO = CompanyDTO(),
)
