package com.inovatech.smartpack.ui.screens.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.api.toUser
import com.inovatech.smartpack.model.toUserRequest
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

    fun refreshUsers() {
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
                    //Actualitzem la llista d'usuaris
                    refreshUsers()
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
                        _uiState.update { it.copy(msg = response.body()!!.msg) }
                    }
                }
                refreshUsers()
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