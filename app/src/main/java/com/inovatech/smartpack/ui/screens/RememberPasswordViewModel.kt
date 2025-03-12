package com.inovatech.smartpack.ui.screens

import androidx.lifecycle.ViewModel
import com.inovatech.smartpack.model.RememberPasswordUiState
import kotlinx.coroutines.flow.*

class RememberPasswordViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RememberPasswordUiState())
    val uiState: StateFlow<RememberPasswordUiState> = _uiState.asStateFlow()

    fun updateEmail (email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }
}