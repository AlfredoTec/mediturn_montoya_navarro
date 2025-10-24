package com.tecsup.mediturn.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DoctorDetailUiState(
    val isLoading: Boolean = false,
    val doctor: Doctor? = null
)

class DoctorDetailViewModel(
    private val repository: DoctorRepository = DoctorRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorDetailUiState())
    val uiState: StateFlow<DoctorDetailUiState> = _uiState.asStateFlow()

    fun loadDoctor(doctorId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val doctor = repository.getDoctorById(doctorId)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                doctor = doctor
            )
        }
    }
}