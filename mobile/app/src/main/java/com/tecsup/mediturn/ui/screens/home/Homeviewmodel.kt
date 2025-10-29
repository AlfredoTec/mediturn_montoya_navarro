package com.tecsup.mediturn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.repository.DoctorRepository
import com.tecsup.mediturn.data.repository.UnifiedDoctorRepository
import com.tecsup.mediturn.data.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val featuredDoctors: List<Doctor> = emptyList()
)

class HomeViewModel(
    private val doctorRepository: DoctorRepository? = null,  // Legacy, ahora opcional
    private val unifiedRepository: UnifiedDoctorRepository? = null  // Nuevo, soporta Room y Retrofit
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadFeaturedDoctors()
    }

    private fun loadFeaturedDoctors() {
        viewModelScope.launch {
            when {
                unifiedRepository != null -> {
                    unifiedRepository.getAllDoctorsFlow().collect { doctors: List<Doctor> ->
                        _uiState.value = _uiState.value.copy(
                            featuredDoctors = doctors.take(6)
                        )
                    }
                }
                doctorRepository != null -> {
                    doctorRepository.getAllDoctorsFlow().collect { doctors: List<Doctor> ->
                        _uiState.value = _uiState.value.copy(
                            featuredDoctors = doctors.take(6)
                        )
                    }
                }
            }
        }
    }
}
