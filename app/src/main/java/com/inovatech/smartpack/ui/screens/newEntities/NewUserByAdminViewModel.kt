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

    fun updateEmail(email: String) {
        _uiState.update { it.copy(newUser = it.newUser.copy(email = email)) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(newUser = it.newUser.copy(password = password)) }
    }

    fun updateSecretWord(secretWord: String) {
        _uiState.update { it.copy(newUser = it.newUser.copy(secretWord = secretWord)) }
    }

    fun updateDni(dni: String) {
        _uiState.update { it.copy(newUser = it.newUser.copy(dni = dni)) }
    }

    fun updateRole(role: Role) {
        _uiState.update { it.copy(newUser = it.newUser.copy(role = role)) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(newUser = it.newUser.copy(name = name)) }
    }

    fun updateSurname(surname: String) {
        _uiState.update { it.copy(newUser = it.newUser.copy(surname = surname)) }
    }

    fun updateTel(tel: String) {
        _uiState.update { it.copy(newUser = it.newUser.copy(tel = tel)) }
    }

    fun updateAddress(address: String) {
        _uiState.update { it.copy(newUser = it.newUser.copy(address = address)) }
    }

    fun createUser() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = smartpackRepository.register(uiState.value.newUser)
            if (response.isSuccessful) {
                _uiState.update { it.copy(msg = "Usuari creat correctament") }
                _uiState.update { it.copy(newUser = UserRequest()) }
            } else {
                _uiState.update { it.copy(msg = "Error ${response.code()}: S'ha produït un error al registrar") }
            }
        }
        _uiState.update { it.copy(isLoading = false) }
    }
}