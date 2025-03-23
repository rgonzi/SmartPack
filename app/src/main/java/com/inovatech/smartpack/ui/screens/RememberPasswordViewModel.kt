package com.inovatech.smartpack.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.ForgotPasswordRequest
import com.inovatech.smartpack.model.RememberPasswordUiState
import com.inovatech.smartpack.model.ResetPasswordRequest
import com.inovatech.smartpack.utils.Settings
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

/**
 * ViewModel associat a la pantalla de recordar contrasenya. Gestiona l'estat i resolt peticions
 * des de la pantalla.
 */
@HiltViewModel
class RememberPasswordViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RememberPasswordUiState())
    val uiState: StateFlow<RememberPasswordUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun updateNewPassword(newPassword: String) {
        _uiState.update {
            it.copy(newPassword = newPassword)
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }

    fun clearFields() {
        _uiState.update {
            it.copy(
                email = "",
                newPassword = "",
                newTokenObtained = false,
                passwordChangedSuccess = false
            )
        }
    }

    fun rememberPassword() {
        _uiState.update { it.copy(isLoading = true) }

        if (_uiState.value.newTokenObtained) {

            //Canviem la contrasenya al servidor amb el nou token
            val newPassword = _uiState.value.newPassword
            if (newPassword.isValidPassword()) {
                val resetPasswordRequest = ResetPasswordRequest(newPassword, tokenRepository.getAuthToken()!!)

                viewModelScope.launch {
                    delay(800)
                    val result = withTimeoutOrNull(TIMEOUT) {
                        try {
                            val response = smartPackRepository.resetPassword(resetPasswordRequest)

                            if (response.isSuccessful) {
                                if (response.body() != null) {
                                    tokenRepository.clearAuthToken()
                                    _uiState.update {
                                        it.copy(passwordChangedSuccess = true, error = null)
                                    }
                                }
                            } else {
                                _uiState.update {
                                    it.copy(error = "Error ${response.code()}: S'ha produït un error inesperat")
                                }
                            }

                        } catch (e: IOException) {
                            _uiState.update {
                                it.copy(error = "No s'ha pogut connectar amb el servidor")
                            }
                            Log.d(Settings.LOG_TAG, e.message.toString())
                        }
                    }
                    if (result == null) {
                        _uiState.update {
                            it.copy(error = "S'ha superat el temps de resposta")
                        }
                    }
                    _uiState.update { it.copy(isLoading = false) }
                }
            } else {
                _uiState.update {
                    it.copy(error = "La contrasenya ha de tenir mínim 8 caràcters, almenys 1 majúscula i 1 número")
                }
            }
        } else {

            //Obtenim un nou token per canviar la contrasenya de l'email proporcionat
            val email = _uiState.value.email
            if (email.isValidEmail()) {
                viewModelScope.launch {
                    val forgotPasswordRequest = ForgotPasswordRequest(email)
                    delay(800)
                    val result = withTimeoutOrNull(TIMEOUT) {
                        try {
                            val response = smartPackRepository.forgotPassword(forgotPasswordRequest)

                            if (response.isSuccessful) {
                                if (response.body() != null) {
                                    val token = response.body()!!.tokenRecovery
                                    tokenRepository.saveAuthToken(token)
                                    Log.d(Settings.LOG_TAG, "Code: ${response.code().toString()}")
                                    Log.d(Settings.LOG_TAG, "Message: ${response.message()}")
                                    Log.d(Settings.LOG_TAG, "Body: ${response.body().toString()}")
                                    Log.d(Settings.LOG_TAG, "Full response: ${response.toString()}")
                                    Log.d(Settings.LOG_TAG, token)
                                    _uiState.update {
                                        it.copy(newTokenObtained = true, error = null)
                                    }
                                }
                            } else {
                                _uiState.update {
                                    it.copy(error = "Error ${response.code()}: S'ha produït un error inesperat")
                                }
                            }

                        } catch (e: IOException) {
                            _uiState.update {
                                it.copy(error = "No s'ha pogut connectar amb el servidor")
                            }
                            Log.d(Settings.LOG_TAG, e.message.toString())
                        }
                    }
                    if (result == null) {
                        _uiState.update {
                            it.copy(error = "S'ha superat el temps de resposta")
                        }
                    }
                    _uiState.update { it.copy(isLoading = false) }
                }
            } else {
                _uiState.update {
                    it.copy(error = "L'email no és correcte")
                }
            }
        }
    }
}