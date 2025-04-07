package com.inovatech.smartpack.ui.screens.deliveryman

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.api.toUser
import com.inovatech.smartpack.model.uiState.DeliveryManUiState
import com.inovatech.smartpack.utils.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DeliveryManHomeViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val smartPackRepository: SmartPackRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(DeliveryManUiState())
    val uiState: StateFlow<DeliveryManUiState> = _uiState.asStateFlow()

    init {
        getDeliverymanDetails()
    }

    fun getDeliverymanDetails() {
        _uiState.update { it.copy(isLoading = true, msg = null) }

        viewModelScope.launch {
            try {
                //TODO Obtenir informació del transportista a partir de la ID d'usuari
//                val response = smartPackRepository.getUserDetails()
//
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//                        val user = response.body()!!.toUser()
//                        _uiState.update { it.copy(deliveryman = user) }
//                    }
//                } else {
//                    _uiState.update {
//                        it.copy(
//                            msg = "Error ${response.code()}: No s'han pogut obtenir les dades"
//                        )
//                    }
//                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(msg = "No s'ha pogut connectar amb el servidor")
                }
                Log.d(Settings.LOG_TAG, e.message.toString())
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}