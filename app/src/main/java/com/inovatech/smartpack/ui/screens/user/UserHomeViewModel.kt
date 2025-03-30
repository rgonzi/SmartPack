package com.inovatech.smartpack.ui.screens.user

import androidx.lifecycle.ViewModel
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel associat a la pantalla principal. Gestiona l'estat i resolt peticions
 * des de la pantalla.
 */
@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /**
     * Mètode que realitza el logout de l'aplicació eliminant el token guardat
     */
    fun logout() {
        tokenRepository.clearAuthToken()

        if (tokenRepository.getAuthToken() == null) {
            _uiState.update { it.copy(isLogoutSuccess = true) }
        }
    }

    fun getUserId() {

    }

    fun getActiveServices() {
        //TODO Obtenir llistat de serveis actius de l'usuari
    }
}