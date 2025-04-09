package com.inovatech.smartpack.ui.screens.deliveryman

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.Deliveryman
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.ServiceStatus
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.Vehicle
import com.inovatech.smartpack.model.api.DeliverymanRequest
import com.inovatech.smartpack.model.api.toDeliveryman
import com.inovatech.smartpack.model.api.toUser
import com.inovatech.smartpack.model.api.toVehicle
import com.inovatech.smartpack.model.toDeliverymanRequest
import com.inovatech.smartpack.model.toVehicleDTO
import com.inovatech.smartpack.model.uiState.DeliveryManUiState
import com.inovatech.smartpack.utils.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.IDN
import javax.inject.Inject

@HiltViewModel
class DeliveryManHomeViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeliveryManUiState())
    val uiState: StateFlow<DeliveryManUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun updateVehicleBrand(brand: String) {
        _uiState.update {
            it.copy(
                deliveryman = it.deliveryman!!.copy(
                    vehicle = it.deliveryman.vehicle.copy(brand = brand)
                )
            )
        }
    }

    fun updateVehicleModel(model: String) {
        _uiState.update {
            it.copy(
                deliveryman = it.deliveryman!!.copy(
                    vehicle = it.deliveryman.vehicle.copy(model = model)
                )
            )
        }
    }

    fun updateVehiclePlate(plate: String) {
        _uiState.update {
            it.copy(
                deliveryman = it.deliveryman!!.copy(
                    vehicle = it.deliveryman.vehicle.copy(plate = plate)
                )
            )
        }
    }

    fun updateLicences(licence: String) {
        _uiState.update { it.copy(deliveryman = it.deliveryman!!.copy(licence = licence)) }
    }

    fun vehicleHasChanged(hasChanged: Boolean) {
        _uiState.update { it.copy(vehicleHasChanged = hasChanged) }
    }

    fun licenceHasChanged(hasChanged: Boolean) {
        _uiState.update { it.copy(licenseHasChanged = hasChanged) }
    }

    fun resetMsg() {
        _uiState.update { it.copy(msg = null) }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val user = getUser()

                if (user == null) {
                    _uiState.update {
                        it.copy(msg = "1Error: No s'han pogut obtenir les dades")
                    }
                    return@launch
                }

                val deliveryman = getDeliverymanDetails(user.id)
                if (deliveryman == null) {
                    createNewDeliveryman(user)
                }

                if (deliveryman?.vehicle == null) {
                    _uiState.update {
                        it.copy(msg = "No hi ha cap vehicle assignat")
                    }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(msg = "No s'ha pogut connectar amb el servidor")
                }
                Log.d(Settings.LOG_TAG, e.message.toString())
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


    private suspend fun getDeliverymanDetails(userId: Long): Deliveryman? {
        val response = smartPackRepository.getDeliverymanByUserId(userId)

        return if (response.isSuccessful) {
            response.body()?.toDeliveryman()?.also { deliveryman ->
                _uiState.update {
                    it.copy(
                        deliveryman = deliveryman
                    )
                }
            }
        } else {
            _uiState.update {
                it.copy(msg = "Error ${response.code()}: No s'han pogut obtenir les dades")
            }
            null
        }
    }

    private suspend fun createNewDeliveryman(user: User): Deliveryman? {
        val newDeliveryman = DeliverymanRequest(
            userId = user.id, licence = ""
        )
        val response = smartPackRepository.createDeliveryman(newDeliveryman)
        return if (response.isSuccessful) {
            response.body()?.toDeliveryman()?.also { deliveryman ->
                _uiState.update { it.copy(deliveryman = deliveryman) }
            }
        } else {
            _uiState.update {
                it.copy(msg = "Error ${response.code()}: No s'ha pogut crear el transportista")
            }
            null
        }
    }

    /**
     * Mètode que obté les dades de l'usuari a partir del token generat al fer
     * login. S'executarà només iniciar aquest ViewModel
     */
    private suspend fun getUser(): User? {
        val response = smartPackRepository.getUserDetails()

        return if (response.isSuccessful) {
            response.body()?.toUser()?.also { user ->
                _uiState.update { it.copy(user = user) }
            }
        } else {
            _uiState.update {
                it.copy(msg = "Error ${response.code()}: No s'han pogut obtenir les dades")
            }
            null
        }
    }

//    private suspend fun getVehicleById(vehicleId: Long): Vehicle? {
//        val response = smartPackRepository.getVehicleById(vehicleId)
//
//        return if (response.isSuccessful) {
//            response.body()?.toVehicle()?.also { vehicle ->
//                _uiState.update { it.copy(vehicle = vehicle) }
//            }
//        } else {
//            _uiState.update {
//                it.copy(msg = "Error ${response.code()}: No s'han pogut obtenir les dades")
//            }
//            null
//        }
//    }

    private fun validateVehicleInput(): Boolean {
        val vehicle = uiState.value.deliveryman!!.vehicle

        if (vehicle.brand.isBlank() || vehicle.model.isBlank() || vehicle.plate.isBlank()) {
            _uiState.update {
                it.copy(
                    msg = "Si us plau, ompliu tots els camps del vehicle.", isLoading = false
                )
            }
            return false
        }
        return true
    }

    fun createVehicle() {
        _uiState.update { it.copy(isLoading = true, msg = null) }

        val vehicle = uiState.value.deliveryman!!.vehicle

        if (validateVehicleInput()) {
            viewModelScope.launch {
                delay(500)
                try {
                    val response = smartPackRepository.createVehicle(vehicle.toVehicleDTO())


                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val newVehicleData = response.body()!!.toVehicle()
                            _uiState.update {
                                it.copy(
                                    deliveryman = it.deliveryman!!.copy(vehicle = newVehicleData)
                                )
                            }
                            val response2 = smartPackRepository.assignVehicleToDeliveryman(
                                uiState.value.deliveryman!!.id, newVehicleData.id
                            )
                            if (response2.isSuccessful) {
                                _uiState.update {
                                    it.copy(
                                        deliveryman = it.deliveryman!!.copy(vehicle = newVehicleData),
                                        msg = "Vehicle creat i assignat correctament"
                                    )
                                }
                            }
                        }
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

                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateVehicle() {
        val vehicle = uiState.value.deliveryman!!.vehicle

        _uiState.update { it.copy(isLoading = true, msg = null) }

        if (validateVehicleInput()) {
            viewModelScope.launch {
                delay(500)
                try {
                    val response =
                        smartPackRepository.updateVehicle(vehicle.id, vehicle.toVehicleDTO())

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val newVehicleData = response.body()!!.toVehicle()
                            _uiState.update {
                                it.copy(
                                    deliveryman = it.deliveryman!!.copy(
                                        vehicle = newVehicleData
                                    ), msg = "Canvis realitzats correctament"
                                )
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                msg = "Error ${response.code()}: No s'ha pogut fer la modificació"
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
    }

    fun updateDeliveryman() {
        val deliveryman = uiState.value.deliveryman!!

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(500)
            try {
                val response = smartPackRepository.updateDeliveryman(
                    deliveryman.id, deliveryman.toDeliverymanRequest()
                )

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _uiState.update {
                            it.copy(msg = "Canvis realitzats correctament")
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            msg = "Error ${response.code()}: No s'ha pogut fer la modificació"
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

    fun deactivateVehicle() {
        val deliveryman = uiState.value.deliveryman!!
        val vehicleId = deliveryman.vehicle.id
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.deactivateVehicle(vehicleId)
                val response2 = smartPackRepository.desassignVehicleFromDeliveryman(deliveryman.id)

                if (response.isSuccessful && response2.isSuccessful) {
                    resetVehicleTextFields()
                }
                _uiState.update {
                    it.copy(msg = response.body()!!.msg)
                }
            } catch (e: IOException) {
                _uiState.update { it.copy(msg = "No s'ha pogut connectar amb el servidor") }
                Log.d(Settings.LOG_TAG, e.message.toString())
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun resetVehicleTextFields() {
        _uiState.update { it.copy(deliveryman = it.deliveryman!!.copy(vehicle = Vehicle())) }
    }

    fun changeStatus(serviceId: Long, status: ServiceStatus) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.changeServiceStatus(serviceId, status)

                if (response.isSuccessful) {
                    _uiState.update {
                        val serviceToMove = it.assignedServices.find { it.id == serviceId }!!
                        it.copy(
                            assignedServices = it.assignedServices.filterNot { it == serviceToMove },
                            finalizedServices = it.finalizedServices + serviceToMove,
                            msg = "Servei modificat correctament")
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            msg = "Error ${response.code()}: No s'ha pogut modificar l'estat del servei"
                        )
                    }
                }
            } catch (e: IOException) {
                _uiState.update { it.copy(msg = "No s'ha pogut connectar amb el servidor") }
                Log.d(Settings.LOG_TAG, e.message.toString())
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}