package com.inovatech.smartpack.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.RegisterRequest
import com.inovatech.smartpack.model.SignUpUiState
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

    private val TAG = "SmartPack-Debug"

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

    private fun validateInputs(): Boolean {
        val email = _uiState.value.email
        val password = _uiState.value.password

        return when {
            email.isEmpty() -> {
                _uiState.update { it.copy(error = "El correu és obligatori") }
                false
            }
            !email.isValidEmail() -> {
                _uiState.update { it.copy(error = "Introdueix un correu vàlid") }
                false
            }
            password.isEmpty() -> {
                _uiState.update { it.copy(error = "El camp de contrasenya és obligatori") }
                false
            }
            !password.isValidPassword() -> {
                _uiState.update { it.copy(error = "La contrasenya ha de tenir mínim 8 caràcters, almenys 1 majúscula i 1 número") }
                false
            }
            _uiState.value.password != _uiState.value.repeatedPassword -> {
                _uiState.update { it.copy(error = "Les contrasenyes no coincideixen") }
                false
            }
            else -> {
                _uiState.update { it.copy(error = null) }
                true
            }
        }
    }

    fun register() {
        _uiState.update { it.copy(hasTriedRegister = true) }

        if (!validateInputs()) return

        val state = _uiState.value

        _uiState.update { it.copy(
            isLoading = true,
            error = null,
            signUpSuccess = false
        ) }


        viewModelScope.launch {
            delay(800)

            /*
             * Mock per iniciar sessió mentre el servidor no estigui implementat.
             */

            if (state.email == "roger@inovatech.com" && state.password == "1234567A") {
                _uiState.update {
                    it.copy(isLoading = false, signUpSuccess = true)
                }
                return@launch
            }

            val usuariPerRegistrar = RegisterRequest(
                email = state.email,
                password = state.password
            )
            val result = withTimeoutOrNull(TIMEOUT) {
                try {
                    val response = smartPackRepository.register(usuariPerRegistrar)

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            _uiState.update {
                                it.copy(signUpSuccess = true, error = null)
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(error = "Aquest usuari ja està registrat")
                        }
                    }

                } catch (e: IOException) {
                    _uiState.update {
                        it.copy(error = "No s'ha pogut connectar amb el servidor")
                    }
                    Log.d(TAG, e.message.toString())
                }
            }
            if (result == null) {
                _uiState.update {
                    it.copy(error = "S'ha superat el temps de resposta")
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun clearFields() {
        _uiState.update {
            it.copy(
                email = "",
                password = "",
                repeatedPassword = "",
                hasTriedRegister = false,
                signUpSuccess = false,
                error = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }
}