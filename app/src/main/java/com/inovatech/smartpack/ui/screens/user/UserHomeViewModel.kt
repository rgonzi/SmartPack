package com.inovatech.smartpack.ui.screens.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.ServiceStatus
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.api.toService
import com.inovatech.smartpack.model.api.toUser
import com.inovatech.smartpack.model.uiState.UserHomeUiState
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
class UserHomeViewModel @Inject constructor(
    private val smartPackRepository: SmartPackRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserHomeUiState())
    val uiState: StateFlow<UserHomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun resetMsg() {
        _uiState.update { it.copy(msg = null) }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                //Obtenim les dades de l'usuari per obtenir el seu userId
                val user = getUser()

                if (user == null) {
                    _uiState.update {
                        it.copy(msg = "1Error: No s'han pogut obtenir les dades")
                    }
                    return@launch
                }

                //Obtenim el llistat de serveis actius
                getServices(user.id)

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

    /**
     * Mètode que obté les dades de l'usuari a partir del token generat al fer
     * login.
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

    private fun getServices(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val response = smartPackRepository.getServicesPerUser(userId)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            //Guardem tots els serveis
                            services = response.body()!!.map {
                                it.toService()
                            },
                            //Guardem els que NO estan finalitzats
                            activeServices = response.body()!!.map {
                                it.toService()
                            }.filter {
                                it.status == ServiceStatus.ORDENAT || it.status == ServiceStatus.TRANSIT || it.status == ServiceStatus.ENVIAT
                            },
                            //Guardem els que ja estan finalitzats
                            oldServices = response.body()!!.map {
                                it.toService()
                            }.filter {
                                it.status == ServiceStatus.ENTREGAT || it.status == ServiceStatus.NO_ENTREGAT || it.status == ServiceStatus.RETORNAT
                            }
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(msg = "No s'han pogut obtenir els serveis")
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

    fun refreshServices() {
        _uiState.update { it.copy(isRefreshing = true) }
        getServices(uiState.value.user!!.id)
        //La funció getActiveServices ja s'encarrega de canviar isRefreshing = false
    }

    fun deleteService(serviceId: Long) {
        //TODO Eliminar un servei
    }
}