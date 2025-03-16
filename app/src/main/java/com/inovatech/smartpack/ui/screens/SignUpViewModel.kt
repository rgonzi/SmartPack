package com.inovatech.smartpack.ui.screens

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.model.SignUpUiState
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun updateRepeatedPassword(repeatedPassword: String) {
        _uiState.update {
            it.copy(repeatedPassword = repeatedPassword)
        }
    }

    fun register() {
        _uiState.update { it.copy(hasTriedRegister = true) }

        val state = _uiState.value

        if (!state.email.isValidEmail() || !state.password.isValidPassword() || state.password != state.repeatedPassword) {
            _uiState.update { it.copy(error = "Revisa les dades introduïdes") }
            return
        }
        viewModelScope.launch {
            //TODO Registrar usuari
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }
}