package com.inovatech.smartpack.ui.screens.userConfig

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.uiState.ChangePasswordUiState
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.toUserRequest
import com.inovatech.smartpack.utils.Settings
import com.inovatech.smartpack.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun updateUserId(userId: Long) {
        _uiState.update { it.copy(user = User(id = userId)) }
    }

    fun updateNewPassword(newPassword: String) {
        _uiState.update { it.copy(newPassword = newPassword) }
    }

    fun updateRepeatedNewPassword(repeatedNewPassword: String) {
        _uiState.update { it.copy(repeatedNewPassword = repeatedNewPassword) }
    }

    fun resetErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetMsg() {
        _uiState.update { it.copy(msg = null) }
    }

    /**
     * Canvia la visibilitat de la contrasenya en el TextField corresponent
     */
    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    fun changePassword() {
        val newPassword = uiState.value.newPassword

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        //Comprovem que la contrasenya tingui un format adequat
        if (!newPassword.isValidPassword()) {
            _uiState.update {
                it.copy(
                    errorMessage = "La contrasenya ha de tenir mínim 8 caràcters, almenys 1 majúscula i 1 número",
                    isLoading = false
                )
            }
            return
        }
        //Comprovem que les dues contrasenyes siguin iguals
        if (uiState.value.newPassword != uiState.value.repeatedNewPassword) {
            _uiState.update {
                it.copy(
                    errorMessage = "Les contrasenyes no coincideixen",
                    isLoading = false
                )
            }
            return
        }


        viewModelScope.launch {
            delay(800)

            try {
                _uiState.update {
                    it.copy(
                        user = it.user!!.copy(password = it.newPassword)
                    )
                }

                val response = smartPackRepository.updateUser(
                    _uiState.value.user!!.id, _uiState.value.user!!.toUserRequest()
                )

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _uiState.update {
                            it.copy(
                                isPasswordModifiedSuccess = true,
                                msg = "Canvi realitzat correctament"
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error ${response.code()}: No s'ha pogut fer la modificació"
                        )
                    }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(errorMessage = "No s'ha pogut connectar amb el servidor")
                }
                Log.d(Settings.LOG_TAG, e.message.toString())
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}