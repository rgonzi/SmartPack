package com.inovatech.smartpack.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.model.SignUpUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun updateEmail (email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun updatePassword (password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun updateRepeatedPassword (repeatedPassword: String) {
        _uiState.update {
            it.copy(repeatedPassword = repeatedPassword)
        }
    }

    fun register() {
        viewModelScope.launch {
            //TODO Registrar usuari
        }
    }
}