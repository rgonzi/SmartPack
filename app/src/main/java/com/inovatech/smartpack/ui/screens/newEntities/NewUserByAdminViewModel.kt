package com.inovatech.smartpack.ui.screens.newEntities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.api.UserRequest
import com.inovatech.smartpack.model.uiState.NewUserByAdminUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewUserByAdminViewModel @Inject constructor(
    private val smartpackRepository: SmartPackRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewUserByAdminUiState())
    val uiState: StateFlow<NewUserByAdminUiState> = _uiState.asStateFlow()

    fun resetMsg() {
        _uiState.value = _uiState.value.copy(msg = null)
    }

    fun createUser(newUser: UserRequest) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = smartpackRepository.register(newUser)
            if (response.isSuccessful) {
                _uiState.update { it.copy(msg = "Usuari creat correctament") }
            } else {
                _uiState.update { it.copy(msg = "Error ${response.code()}: S'ha produït un error al registrar") }
            }
        }
        _uiState.update { it.copy(isLoading = false) }
    }
}