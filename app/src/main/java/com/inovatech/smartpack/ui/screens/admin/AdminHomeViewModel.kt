package com.inovatech.smartpack.ui.screens.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.*
import com.inovatech.smartpack.model.api.AssignUserToCompanyRequest
import com.inovatech.smartpack.model.api.toCompany
import com.inovatech.smartpack.model.api.toDeliveryman
import com.inovatech.smartpack.model.api.toService
import com.inovatech.smartpack.model.api.toUser
import com.inovatech.smartpack.model.api.toVehicle
import com.inovatech.smartpack.model.uiState.AdminHomeUiState
import com.inovatech.smartpack.utils.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminHomeUiState())
    val uiState: StateFlow<AdminHomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun resetMsg() {
        _uiState.update { it.copy(msg = null) }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getAllUsers()
            getAllDeliverymen()
            getAllVehicles()
            getAllCompanies()
            getAllServices()
            //getAllInvoices()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun getAllServices() {
        viewModelScope.launch {
            try {
                val response = smartPackRepository.getAllServices()
                if (response.isSuccessful && response.body() != null) {
                    val services = response.body()!!.map { it.toService() }
                    _uiState.update { it.copy(servicesList = services) }
                } else {
                    _uiState.update {
                        it.copy(
                            msg = "Error ${response.code()}: No s'han pogut carregar els serveis"
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

    private fun getAllUsers() {
        viewModelScope.launch {
            try {
                val response = smartPackRepository.getAllUsers()
                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!.map { it.toUser() }
                    _uiState.update { it.copy(usersList = users) }
                } else {
                    _uiState.update {
                        it.copy(
                            msg = "Error ${response.code()}: No s'han pogut carregar els usuaris"
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

    private fun getAllDeliverymen() {
        viewModelScope.launch {
            try {
                val response = smartPackRepository.getAllDeliverymen()
                if (response.isSuccessful && response.body() != null) {
                    val deliverymen = response.body()!!.map { it.toDeliveryman() }
                    _uiState.update { it.copy(deliverymenList = deliverymen) }
                } else {
                    _uiState.update {
                        it.copy(
                            msg = "Error ${response.code()}: No s'han pogut carregar les empreses"
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

    private fun getAllVehicles() {
        viewModelScope.launch {
            try {
                val response = smartPackRepository.getAllVehicles()
                if (response.isSuccessful && response.body() != null) {
                    val vehicles = response.body()!!.map { it.toVehicle() }
                    _uiState.update { it.copy(vehiclesList = vehicles) }
                } else {
                    _uiState.update {
                        it.copy(
                            msg = "Error ${response.code()}: No s'han pogut carregar els vehicles"
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

    private fun getAllCompanies() {
        viewModelScope.launch {
            try {
                val response = smartPackRepository.getAllCompanies()
                if (response.isSuccessful && response.body() != null) {
                    val companies = response.body()!!.map { it.toCompany() }
                    _uiState.update { it.copy(companiesList = companies) }
                } else {
                    _uiState.update {
                        it.copy(
                            msg = "Error ${response.code()}: No s'han pogut carregar les empreses"
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

    fun refreshAll() {
        _uiState.update { it.copy(isRefreshing = true) }

        getAllUsers()
        getAllVehicles()
        getAllCompanies()
        getAllServices()

        //TODO Incorporar la funció per obtenir totes les factures

        _uiState.update { it.copy(isRefreshing = false) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onUserSelected(user: User?) {
        _uiState.update { it.copy(selectedUser = user) }
    }

    fun onVehicleSelected(vehicle: Vehicle?) {
        _uiState.update { it.copy(selectedVehicle = vehicle) }
    }

    fun onCompanySelected(company: Company?) {
        _uiState.update { it.copy(selectedCompany = company) }
    }

    fun onServiceSelected(service: Service?) {
        _uiState.update { it.copy(selectedService = service) }
    }

    fun updateUser(updatedUser: User) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response =
                    smartPackRepository.updateUser(updatedUser.id, updatedUser.toUserRequest())

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            selectedUser = null, msg = "Usuari modificat correctament"
                        )
                    }
                    refreshAll()
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

    fun updateService(updatedService: Service) {

        viewModelScope.launch {
            try {
                val response = smartPackRepository.updateService(
                    serviceId = updatedService.id, newService = updatedService.toServiceDTO()
                )

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            selectedVehicle = null, msg = "Servei modificat correctament"
                        )
                    }
                    refreshAll()
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

    fun updateVehicle(updatedVehicle: Vehicle) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.updateVehicle(
                    updatedVehicle.id, updatedVehicle.toVehicleDTO()
                )

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            selectedVehicle = null, msg = "Vehicle modificat correctament"
                        )
                    }
                    refreshAll()
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

    fun updateCompany(updatedCompany: Company) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.updateCompany(
                    updatedCompany.id, updatedCompany.toCompanyDTO()
                )

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            selectedCompany = null, msg = "Empresa modificada correctament"
                        )
                    }
                    refreshAll()
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

    fun deactivateUser(userId: Long) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.deactivateUser(userId)

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _uiState.update {
                            it.copy(
                                msg = response.body()?.msg ?: "Usuari desactivat correctament"
                            )
                        }
                        refreshAll()
                    }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(msg = "No s'ha pogut connectar amb el servidor")
                }
                Log.d(Settings.LOG_TAG, e.message.toString())
            }
            _uiState.update { it.copy(isLoading = false, selectedUser = null) }
        }
    }

    fun deactivateService(serviceId: Long) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.deactivateService(serviceId)

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _uiState.update {
                            it.copy(
                                msg = response.body()?.msg ?: "Usuari desactivat correctament"
                            )
                        }
                        refreshAll()
                    }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(msg = "No s'ha pogut connectar amb el servidor")
                }
                Log.d(Settings.LOG_TAG, e.message.toString())
            }
            _uiState.update { it.copy(isLoading = false, selectedUser = null) }
        }
    }

    fun deactivateCompany(companyId: Long) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.deactivateCompany(companyId)

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _uiState.update {
                            it.copy(
                                msg = response.body()?.msg ?: "Empresa desactivada correctament"
                            )
                        }
                        refreshAll()
                    }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(msg = "No s'ha pogut connectar amb el servidor")
                }
                Log.d(Settings.LOG_TAG, e.message.toString())
            }
            _uiState.update { it.copy(isLoading = false, selectedCompany = null) }
        }
    }

    fun assignVehicle(deliverymanId: Long, vehicleId: Long) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response =
                    smartPackRepository.assignVehicleToDeliveryman(deliverymanId, vehicleId)

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            msg = "Vehicle assignat correctament",
                            deliverymenList = it.deliverymenList.map { deliveryman ->
                                if (deliveryman.id == deliverymanId) deliveryman.copy(
                                    vehicle = Vehicle(
                                        id = vehicleId
                                    )
                                )
                                else deliveryman
                            })
                    }
                    refreshAll()
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

    fun assignCompany(userId: Long, companyId: Long) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.assignUserToCompany(
                    AssignUserToCompanyRequest(
                        userId = userId,
                        companyId = companyId
                    )
                )
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            msg = "Empresa assignada correctament",
                            usersList = it.usersList.map { user ->
                                if (user.id == userId) user.copy(
                                    companyId = companyId
                                )
                                else user
                            })
                    }
                    refreshAll()
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

    fun deassignCompany(userId: Long) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.desassignUserFromCompany(userId)

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            msg = "Empresa desassignada correctament",
                            usersList = it.usersList.map { user ->
                                if (user.id == userId) user.copy(
                                    companyId = null
                                )
                                else user
                            })
                    }
                    refreshAll()
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

    fun deassignVehicle(deliverymanId: Long) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.desassignVehicleFromDeliveryman(deliverymanId)

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            msg = "Vehicle desassignat correctament",
                            deliverymenList = it.deliverymenList.map { deliveryman ->
                                if (deliveryman.id == deliverymanId) deliveryman.copy(
                                    vehicle = null
                                )
                                else deliveryman
                            })
                    }
                    refreshAll()
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

    fun deactivateVehicle(vehicleId: Long) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = smartPackRepository.deactivateVehicle(vehicleId)

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _uiState.update {
                            it.copy(
                                msg = response.body()?.msg ?: "Vehicle desactivat correctament"
                            )
                        }
                        refreshAll()
                    }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(msg = "No s'ha pogut connectar amb el servidor")
                }
                Log.d(Settings.LOG_TAG, e.message.toString())
            }
            _uiState.update { it.copy(isLoading = false, selectedVehicle = null) }
        }
    }
}