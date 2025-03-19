package com.inovatech.smartpack.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.RegisterRequest
import com.inovatech.smartpack.model.SignUpUiState
import com.inovatech.smartpack.model.Usuari
import com.inovatech.smartpack.utils.Settings.TIMEOUT
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {
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
        _uiState.update { it.copy(hasTriedRegister = true, error = null) }

        val state = _uiState.value

        if (!state.email.isValidEmail() || !state.password.isValidPassword() || state.password != state.repeatedPassword) {
            _uiState.update { it.copy(error = "Revisa les dades introduïdes") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        val usuariPerRegistrar = RegisterRequest(
            email = state.email,
            password = state.password
        )

        viewModelScope.launch {
            delay(800)
            val result = withTimeoutOrNull(TIMEOUT) {
                try {
                    val response = smartPackRepository.register(usuariPerRegistrar)

                    if (response.isSuccessful && response.body() != null) {
                        _uiState.update {
                            it.copy(signUpSuccess = true, error = null)
                        }
                    } else {
                        _uiState.update {
                            it.copy(error = "S'ha produït un error: ${response.code()}")
                        }
                    }

                } catch (e: IOException) {
                    _uiState.update {
                        it.copy(error = "S'ha produït un error en la petició: ${e.message}")
                    }
                }
            }
            if (result == null) {
                _uiState.update {
                    it.copy(error = "S'ha produït un error: S'ha superat el temps de resposta")
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }
}