package com.tecsup.mediturn.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.repository.DoctorRepository
import com.tecsup.mediturn.data.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DoctorDetailUiState(
    val doctor: Doctor? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class DoctorDetailViewModel(
    private val doctorRepository: DoctorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorDetailUiState())
    val uiState: StateFlow<DoctorDetailUiState> = _uiState.asStateFlow()

    fun loadDoctor(doctorId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val doctor = doctorRepository.getDoctorById(doctorId)

            _uiState.value = if (doctor != null) {
                _uiState.value.copy(
                    doctor = doctor,
                    isLoading = false,
                    error = null
                )
            } else {
                _uiState.value.copy(
                    doctor = null,
                    isLoading = false,
                    error = "Doctor no encontrado"
                )
            }
        }
    }
}