package com.inovatech.smartpack.ui.screens.newObjects

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.Package
import com.inovatech.smartpack.model.ServiceStatus
import com.inovatech.smartpack.model.api.ServiceDTO
import com.inovatech.smartpack.model.api.toUser
import com.inovatech.smartpack.model.toPackageDTO
import com.inovatech.smartpack.model.uiState.NewServiceUiState
import com.inovatech.smartpack.utils.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewServiceViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewServiceUiState())
    val uiState: StateFlow<NewServiceUiState> = _uiState.asStateFlow()

    init {
        getUserId()
    }

    /**
     * Mètode que obté les dades de l'usuari a partir del token generat al fer
     * login. S'executarà només iniciar aquest ViewModel
     */
    private fun getUserId() {
        _uiState.update { it.copy(isLoading = true) }

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
                            msg = "Error ${response.code()}: No s'han pogut obtenir les dades de l'usuari"
                        )
                    }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(msg = "No s'ha pogut connectar amb el servidor")
                }
                Log.d(Settings.LOG_TAG, e.message.toString())
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateRecipientName(name: String) {
        _uiState.update { it.copy(newPackage = it.newPackage.copy(recipientName = name)) }
    }

    fun updateRecipientAddress(address: String) {
        _uiState.update { it.copy(newPackage = it.newPackage.copy(recipientAddress = address)) }
    }

    fun updateRecipientPhone(phone: String) {
        _uiState.update { it.copy(newPackage = it.newPackage.copy(recipientPhone = phone)) }
    }

    fun updateDimensions(dimensions: String) {
        _uiState.update { it.copy(newPackage = it.newPackage.copy(dimensions = dimensions)) }
    }

    fun updateWeight(weight: Int) {
        _uiState.update { it.copy(newPackage = it.newPackage.copy(weight = weight)) }
    }

    fun updateDetails(details: String) {
        _uiState.update { it.copy(newPackage = it.newPackage.copy(details = details)) }
    }

    fun resetMsg() {
        _uiState.update { it.copy(msg = null) }
    }

    private fun validateFields(): Boolean {

        val newPackage = _uiState.value.newPackage

        val fields = listOf(
            newPackage.recipientName,
            newPackage.recipientAddress,
            newPackage.recipientPhone,
            newPackage.dimensions,
            newPackage.weight.toString()
        )

        if (fields.any { it.isBlank() }) {
            _uiState.update { it.copy(msg = "No s'han introduït totes les dades") }
            return false
        }
        if (newPackage.weight < 1) {
            _uiState.update { it.copy(msg = "El pes ha de ser superior a 0Kg") }
            return false
        }
        return true
    }

    fun createNewService() {
        _uiState.update { it.copy(isLoading = true, hasTriedToCreateService = true) }

        if (validateFields()) {
            val newPackage = _uiState.value.newPackage

            viewModelScope.launch {
                try {
                    val response = smartPackRepository.createService(
                        ServiceDTO(
                            userId = _uiState.value.user?.id,
                            status = ServiceStatus.ORDENAT,
                            packageRequest = newPackage.toPackageDTO()
                        )
                    )
                    if (response.isSuccessful) {
                        _uiState.update {
                            it.copy(msg = "Servei creat correctament")
                        }
                        clearFields()
                    } else {
                        _uiState.update {
                            it.copy(
                                msg = "Error ${response.code()}: No s'ha pogut crear el servei"
                            )
                        }
                    }
                } catch (e: IOException) {
                    _uiState.update {
                        it.copy(msg = "No s'ha pogut connectar amb el servidor")
                    }
                    Log.d(Settings.LOG_TAG, e.message.toString())
                }
            }
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    private fun clearFields() {
        _uiState.update {
            it.copy(
                newPackage = Package(),
                hasTriedToCreateService = false
            )
        }
    }
}