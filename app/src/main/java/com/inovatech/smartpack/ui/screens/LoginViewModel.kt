package com.inovatech.smartpack.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.LoginUiState
import com.inovatech.smartpack.network.RetrofitInstance
import com.inovatech.smartpack.utils.Settings.TIMEOUT
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okio.IOException

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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

    fun login(context: Context) {
        _uiState.update { it.copy(hasTriedLogin = true, error = null) }

        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isEmpty() || password.isEmpty()) {
            _uiState.update {
                it.copy(error = "Omple tots els camps")
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        if (!email.isValidEmail() || !password.isValidPassword()) {
            _uiState.update {
                it.copy(isLoading = false, error = "Revisa les dades introduïdes")
            }
            return
        }

        viewModelScope.launch {
            val storage = TokenRepository(context)
            val result = withTimeoutOrNull(TIMEOUT) {
//                try {
//                    val response = RetrofitInstance.smartPackRepository.login(
//                        email = email, password = password
//                    )
//                    if (response.isSuccessful && response.body() != null) {
//                        val loginResponse = response.body()!!
//                        _uiState.update {
//                            it.copy(token = loginResponse.token, error = null)
//                        }
//                        storage.saveAuthToken(token = loginResponse.token)
//                    } else {
//                        _uiState.update {
//                            it.copy(error = "S'ha produït un error: ${response.code()}")
//                        }
//                    }
//
//                } catch (e: IOException) {
//                    _uiState.update {
//                        it.copy(error = "S'ha produït un error en la petició: ${e.message}")
//                    }
//                }
            }
            if (result == null) {
                _uiState.update {
                    it.copy(error = "S'ha produït un error: S'ha superat el temps de resposta")
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}