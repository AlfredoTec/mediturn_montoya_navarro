package com.tecsup.mediturn.ui.screens.home

import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.repository.DoctorRepository
import com.tecsup.mediturn.data.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val featuredDoctors: List<Doctor> = emptyList()
)

class HomeViewModel : ViewModel() {
    private val doctorRepository = DoctorRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadFeaturedDoctors()
    }

    private fun loadFeaturedDoctors() {
        val doctors = doctorRepository.getAllDoctors().take(6)
        _uiState.value = _uiState.value.copy(featuredDoctors = doctors)
    }
}