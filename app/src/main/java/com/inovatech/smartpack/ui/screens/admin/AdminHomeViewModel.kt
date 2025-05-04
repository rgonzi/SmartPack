package com.inovatech.smartpack.ui.screens.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.Company
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.Vehicle
import com.inovatech.smartpack.model.api.toUser
import com.inovatech.smartpack.model.toCompanyDTO
import com.inovatech.smartpack.model.toUserRequest
import com.inovatech.smartpack.model.toVehicleDTO
import com.inovatech.smartpack.model.uiState.AdminHomeUiState
import com.inovatech.smartpack.utils.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminHomeUiState())
    val uiState: StateFlow<AdminHomeUiState> = _uiState.asStateFlow()

    init {
        getAllUsers()
    }

    fun resetMsg() {
        _uiState.update { it.copy(msg = null) }
    }

    private fun getAllUsers() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = smartPackRepository.getAllUsers()
                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!.map { it.toUser() }
                    _uiState.update { it.copy(usersList = users, isLoading = false) }
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
            _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
        }
    }

    private fun getAllVehicles() {
        //TODO: Implementar obtenir vehicles
//        _uiState.update { it.copy(isLoading = true) }
//        viewModelScope.launch {
//            try {
//                val response = smartPackRepository.getAllVehicles()
//                if (response.isSuccessful && response.body() != null) {
//                    val vehicles = response.body()!!.map { it.toVehicle() }
//                    _uiState.update { it.copy(vehiclesList = vehicles, isLoading = false) }
//                } else {
//                    _uiState.update {
//                        it.copy(
//                            msg = "Error ${response.code()}: No s'han pogut carregar els vehicles"
//                        )
//                    }
//                }
//            } catch (e: IOException) {
//                _uiState.update {
//                    it.copy(msg = "No s'ha pogut connectar amb el servidor")
//                }
//                Log.d(Settings.LOG_TAG, e.message.toString())
//            }
//            _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
//        }
    }

    private fun getAllCompanies() {
        //TODO: Implementar obtenir empreses
    }

    fun refreshAll() {
        _uiState.update { it.copy(isRefreshing = true) }

        getAllUsers()

        //La funció getAllUsers() ja s'encarrega de canviar isRefreshing = false
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

    fun updateVehicle(updatedVehicle: Vehicle) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response =
                    smartPackRepository.updateVehicle(
                        updatedVehicle.id,
                        updatedVehicle.toVehicleDTO()
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
                val response =
                    smartPackRepository.updateCompany(
                        updatedCompany.id,
                        updatedCompany.toCompanyDTO()
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