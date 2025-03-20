package com.inovatech.smartpack.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.RegisterRequest
import com.inovatech.smartpack.model.Role
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

/**
 * ViewModel associat a la pantalla de registre. Gestiona l'estat i resolt peticions
 * des de la pantalla.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val TAG = "SmartPack-Debug"

    /**
     * Actualitza l'input del TextField corresponent en el UiState i obliga a la UI a recomposar-se
     */
    fun updateField(field: String, value: String) {
        when (field) {
            "email" -> _uiState.update { it.copy(email = value) }
            "password" -> _uiState.update { it.copy(password = value) }
            "repeatedPassword" -> _uiState.update { it.copy(repeatedPassword = value) }
            "name" -> _uiState.update { it.copy(name = value) }
            "surname" -> _uiState.update { it.copy(surname = value) }
            "tel" -> _uiState.update { it.copy(tel = value) }
            "address" -> _uiState.update { it.copy(address = value) }
        }
    }
    /**
     * Mètode que valida que els inputs als TextField de la pantalla siguin correctes, validant que
     * no estiguin buits, que les dues contrasenyes coincideixin i que compleixen un patró en Regex
     */
    private fun validateInputs(): Boolean {
        val email = _uiState.value.email
        val password = _uiState.value.password
        val repeatedPassword = _uiState.value.repeatedPassword
        val name = _uiState.value.name
        val surname = _uiState.value.surname
        val tel = _uiState.value.tel
        val address = _uiState.value.address

        return when {
            name.isEmpty() || surname.isEmpty() || tel.isEmpty() || address.isEmpty() -> {
                _uiState.update { it.copy(error = "Tots els camps són obligatoris") }
                false
            }
            !email.isValidEmail() -> {
                _uiState.update { it.copy(error = "Introdueix un correu vàlid") }
                false
            }
            !password.isValidPassword() -> {
                _uiState.update { it.copy(error = "La contrasenya ha de tenir mínim 8 caràcters, almenys 1 majúscula i 1 número") }
                false
            }
            password != repeatedPassword -> {
                _uiState.update { it.copy(error = "Les contrasenyes no coincideixen") }
                false
            }
            else -> {
                _uiState.update { it.copy(error = null) }
                true
            }
        }
    }

    /**
     * Mètode que realitza la petició de registre al servidor desprñes de validar la informació
     * dels inputs. Inclou un Mock per provar la funcionalitat quan no tinguem internet o
     * accés a l'API.
     */
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
                password = state.password,
                role = Role.ROLE_USER,
                name = state.name,
                surname = state.surname,
                tel = state.tel,
                address = state.address
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
                            it.copy(error = "Error ${response.code()}: S'ha produït un error al registrar")
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

    /**
     * Funció que reseteja l'estat de la UiState associada
     */
    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }
}