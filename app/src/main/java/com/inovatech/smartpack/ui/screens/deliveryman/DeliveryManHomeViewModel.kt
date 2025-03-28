package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.lifecycle.ViewModel
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.DeliveryManUiState
import com.inovatech.smartpack.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DeliveryManHomeViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(DeliveryManUiState())
    val uiState: StateFlow<DeliveryManUiState> = _uiState.asStateFlow()

    fun logout() {
        tokenRepository.clearAuthToken()

        if (tokenRepository.getAuthToken() == null) {
            _uiState.update { it.copy(isLogoutSuccess = true) }
        }
    }
}