package com.inovatech.smartpack.ui.screens.newEntities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.Vehicle
import com.inovatech.smartpack.model.toVehicleDTO
import com.inovatech.smartpack.model.uiState.NewVehicleUiState
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
class NewVehicleViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewVehicleUiState())
    val uiState: StateFlow<NewVehicleUiState> = _uiState.asStateFlow()

    fun updateBrand(brand: String) {
        _uiState.update { it.copy(newVehicle = it.newVehicle.copy(brand = brand)) }
    }

    fun updateModel(model: String) {
        _uiState.update { it.copy(newVehicle = it.newVehicle.copy(model = model)) }
    }

    fun updatePlate(plate: String) {
        _uiState.update { it.copy(newVehicle = it.newVehicle.copy(plate = plate)) }
    }

    fun resetMsg() {
        _uiState.update { it.copy(msg = null) }
    }

    private fun validateFields(): Boolean {
        val newVehicle = _uiState.value.newVehicle
        val fields = listOf(newVehicle.brand, newVehicle.model, newVehicle.plate)
        if (fields.any { it.isBlank() }) {
            _uiState.update { it.copy(msg = "No s'han introduït totes les dades") }
            return false
        }
        return true
    }

    fun createNewVehicle() {
        _uiState.update { it.copy(isLoading = true, hasTriedToCreateVehicle = true) }

        if (validateFields()) {
            val newVehicle = _uiState.value.newVehicle

            viewModelScope.launch {
                try {
                    val response = smartPackRepository.createVehicle(
                        newVehicle.toVehicleDTO()
                    )
                    if (response.isSuccessful) {
                        _uiState.update {
                            it.copy(msg = "Vehicle creat correctament")
                        }
                        clearFields()
                    } else {
                        _uiState.update {
                            it.copy(
                                msg = "Error ${response.code()}: No s'ha pogut crear el vehicle"
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
                newVehicle = Vehicle(),
                hasTriedToCreateVehicle = false
            )
        }
    }
}