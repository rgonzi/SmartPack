package com.inovatech.smartpack.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.api.UserRequest
import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.model.uiState.SignUpUiState
import com.inovatech.smartpack.utils.Settings
import com.inovatech.smartpack.utils.Settings.LOGIN_TIMEOUT
import com.inovatech.smartpack.utils.isValidDni
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


    /**
     * Actualitza l'input del TextField corresponent en el UiState i obliga a la UI a recomposar-se
     */
    fun updateField(field: String, value: String) {
        when (field) {
            "email" -> _uiState.update { it.copy(email = value) }
            "password" -> _uiState.update { it.copy(password = value) }
            "repeatedPassword" -> _uiState.update { it.copy(repeatedPassword = value) }
            "secretWord" -> _uiState.update { it.copy(secretWord = value) }
            "dni" -> _uiState.update { it.copy(dni = value) }
            "name" -> _uiState.update { it.copy(name = value) }
            "surname" -> _uiState.update { it.copy(surname = value) }
            "tel" -> _uiState.update { it.copy(tel = value) }
            "addressType" -> _uiState.update { it.copy(addressType = value) }
            "address" -> _uiState.update { it.copy(address = value) }
            "license" -> _uiState.update { it.copy(license = value) }

        }
    }

    fun updateRole(newRole: Role) {
        _uiState.update { it.copy(role = newRole) }
    }

    /**
     * Mètode que valida que els inputs als TextField de la pantalla siguin correctes, validant que
     * no estiguin buits, que les dues contrasenyes coincideixin i que compleixen un patró en Regex
     */
    private fun validateInputs(): Boolean {
        val email = _uiState.value.email
        val password = _uiState.value.password
        val repeatedPassword = _uiState.value.repeatedPassword
        val dni = _uiState.value.dni
        val name = _uiState.value.name
        val surname = _uiState.value.surname
        val tel = _uiState.value.tel
        val address = _uiState.value.address
        val addressType = _uiState.value.addressType
        val secretWord = _uiState.value.secretWord
        val license = _uiState.value.license

        return when {
            name.isEmpty() || surname.isEmpty() || tel.isEmpty() || address.isEmpty() || addressType.isEmpty() || dni.isEmpty() || secretWord.isEmpty() -> {
                _uiState.update { it.copy(error = "Tots els camps són obligatoris") }
                false
            }

            license.isEmpty() && _uiState.value.role == Role.ROLE_DELIVERYMAN -> {
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

            !dni.isValidDni() -> {
                _uiState.update { it.copy(error = "El DNI no té el format correcte") }
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

        _uiState.update {
            it.copy(
                isLoading = true, error = null, signUpSuccess = false
            )
        }
        viewModelScope.launch {
            delay(800)

            val usuariPerRegistrar = UserRequest(
                email = state.email,
                password = state.password,
                secretWord = state.secretWord,
                dni = state.dni,
                role = state.role,
                name = state.name,
                surname = state.surname,
                tel = state.tel,
                //Concatenem el tipus de via amb el nom de la via
                address = state.addressType + " " + state.address
            )
            val result = withTimeoutOrNull(LOGIN_TIMEOUT) {
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
    }

    fun clearFields() {
        _uiState.update { SignUpUiState() }
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