package com.inovatech.smartpack.ui.screens

import androidx.lifecycle.ViewModel
import com.inovatech.smartpack.model.RememberPasswordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * ViewModel associat a la pantalla de recordar contrasenya. Gestiona l'estat i resolt peticions
 * des de la pantalla.
 */
@HiltViewModel
class RememberPasswordViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(RememberPasswordUiState())
    val uiState: StateFlow<RememberPasswordUiState> = _uiState.asStateFlow()

    fun updateEmail (email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    //TODO Petició per recordar contrasenya
}