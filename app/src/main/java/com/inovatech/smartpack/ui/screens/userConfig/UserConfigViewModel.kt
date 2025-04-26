package com.inovatech.smartpack.ui.screens.userConfig

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.api.toUser
import com.inovatech.smartpack.model.toUserRequest
import com.inovatech.smartpack.model.uiState.UserConfigUiState
import com.inovatech.smartpack.utils.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * ViewModel associat a la pantalla principal. Gestiona l'estat i resolt peticions
 * des de la pantalla.
 */
@HiltViewModel
class UserConfigViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserConfigUiState())
    val uiState: StateFlow<UserConfigUiState> = _uiState.asStateFlow()

    init {
        getUserId()
    }

    fun updateName(name: String) = _uiState.update { it.copy(user = it.user!!.copy(name = name)) }
    fun updateSurname(surname: String) =
        _uiState.update { it.copy(user = it.user!!.copy(surname = surname)) }

    fun updateTel(tel: String) = _uiState.update { it.copy(user = it.user!!.copy(tel = tel)) }
    fun updateAddress(address: String) =
        _uiState.update { it.copy(user = it.user!!.copy(address = address)) }

    fun updateObservations(observations: String) {
        _uiState.update { it.copy(user = it.user!!.copy(observations = observations)) }
    }


    fun resetErrorMessage() { _uiState.update { it.copy(errorMessage = null) }}
    fun resetMsg() {_uiState.update { it.copy(msg = null) }}

    /**
     * Mètode que obté les dades de l'usuari a partir del token generat al fer
     * login. S'executarà només iniciar aquest ViewModel
     */
    private fun getUserId() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.getUserDetails()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val user = response.body()!!.toUser()
                        _uiState.update { it.copy(user = user) }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error ${response.code()}: No s'han pogut obtenir les dades de l'usuari"
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

    fun saveChanges() {
        val user = uiState.value.user!!

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            delay(800)
            try {
                val response = smartPackRepository.updateUser(user.id, user.toUserRequest())

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val newUserData = response.body()!!.toUser()
                        _uiState.update {
                            it.copy(
                                user = newUserData,
                                msg = "Canvis realitzats correctament"
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

    /**
     * Mètode que realitza el logout de l'aplicació eliminant el token guardat
     */
    fun logout() {
        tokenRepository.clearAuthToken()

        if (tokenRepository.getAuthToken() == null) {
            _uiState.update { it.copy(isLogoutOrDeactivateSuccess = true) }
        }
    }

    fun deactivateAccount() {
        val id = uiState.value.user!!.id
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.deactivateUser(id)

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _uiState.update {
                            it.copy(
                                isLogoutOrDeactivateSuccess = true,
                                msg = response.body()!!.msg
                            )
                        }
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