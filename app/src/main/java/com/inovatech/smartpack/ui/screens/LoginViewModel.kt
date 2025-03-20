package com.inovatech.smartpack.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.LoginUiState
import com.inovatech.smartpack.model.LoginRequest
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
class LoginViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val TAG = "SmartPack-Debug"

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(passwordVisible = !it.passwordVisible)
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
            else -> {
                _uiState.update { it.copy(error = null) }
                true
            }
        }
    }

    fun login() {

        _uiState.update { it.copy(hasTriedLogin = true) }

        if (!validateInputs()) return

        _uiState.update {
            it.copy(
                error = null,
                isLoading = true,
                loginSuccess = false
            )
        }

        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        viewModelScope.launch {
            /*
             * Mock per iniciar sessió mentre el servidor no estigui implementat.
             * També simulem latència
             */

            delay(800) //Simulacio de latencia

            if (email == "roger@inovatech.com" && password == "1234567A") {
                val token = "29uht4rg246iejsh9834tyhr563gf"
                tokenRepository.saveAuthToken(token)
                _uiState.update {
                    it.copy(isLoading = false, loginSuccess = true)
                }
                return@launch
            }

            val usuariLogin = LoginRequest(email = email, password = password)

            val result = withTimeoutOrNull(TIMEOUT) {
                try {
                    val response = smartPackRepository.login(usuariLogin)

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val loginResponse = response.body()!!
                            tokenRepository.saveAuthToken(token = loginResponse.token)

                            _uiState.update {
                                it.copy(
                                    error = null,
                                    loginSuccess = true
                                )
                            }
                        }
                    } else {
                        if(response.code() == 401) {
                            _uiState.update {
                                it.copy(
                                    error = "Error: Credencials incorrectes"
                                )
                            }
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
                hasTriedLogin = false,
                loginSuccess = false,
                error = null
            )
        }
    }
}