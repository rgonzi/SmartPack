package com.inovatech.smartpack.ui.screens.newEntities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.api.CompanyDTO
import com.inovatech.smartpack.model.uiState.NewCompanyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewCompanyViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewCompanyUiState())
    val uiState: StateFlow<NewCompanyUiState> = _uiState.asStateFlow()

    fun resetMsg() {
        _uiState.update { it.copy(msg = null) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(newCompany = it.newCompany.copy(email = email)) }
    }

    fun updateNif(nif: String) {
        _uiState.update { it.copy(newCompany = it.newCompany.copy(nif = nif)) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(newCompany = it.newCompany.copy(name = name)) }
    }

    fun updateTel(phone: String) {
        _uiState.update { it.copy(newCompany = it.newCompany.copy(phone = phone)) }
    }

    fun updateAddress(address: String) {
        _uiState.update { it.copy(newCompany = it.newCompany.copy(address = address)) }
    }

    fun createCompany() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = smartPackRepository.createCompany(uiState.value.newCompany)
            if (response.isSuccessful) {
                _uiState.update {
                    it.copy(
                        msg = "Empresa creada correctament", newCompany = CompanyDTO()
                    )
                }
            } else {
                _uiState.update { it.copy(msg = "Error ${response.code()}: S'ha produït un error al registrar") }
            }
        }
        _uiState.update { it.copy(isLoading = false) }
    }
}