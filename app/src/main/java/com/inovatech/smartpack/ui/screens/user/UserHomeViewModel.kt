package com.inovatech.smartpack.ui.screens.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.api.toUser
import com.inovatech.smartpack.model.uiState.UserHomeUiState
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


@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserHomeUiState())
    val uiState: StateFlow<UserHomeUiState> = _uiState.asStateFlow()

    init {
        getUserId()
    }
    /**
     * Mètode que obté les dades de l'usuari a partir del token generat al fer
     * login. S'executarà només iniciar aquest ViewModel
     */
    fun getUserId() {

        viewModelScope.launch {
//            delay(300)
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
                            errorMessage = "Error ${response.code()}: No s'han pogut obtenir les dades"
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