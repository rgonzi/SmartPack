package com.inovatech.smartpack.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.model.LoginUiState
import com.inovatech.smartpack.network.RetrofitInstance
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

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

    fun login() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }
            try {
                val email = _uiState.value.email
                val password = _uiState.value.password

                //TODO Validar camps amb regex

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    val response = RetrofitInstance.smartPackRepository.login(
                        email = email, password = password
                    )
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        _uiState.update {
                            it.copy(token = loginResponse?.token, error = null)
                        }
                    } else {
                        _uiState.update {
                            it.copy(error = "S'ha produït un error: ${response.code()}")
                        }
                    }
                }
            } catch (e: HttpException) {
                _uiState.update {
                    it.copy(error = "S'ha produït un error: ${e.message()}")
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(error = "S'ha produït un error de connexió: ${e.message}")
                }
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }
}