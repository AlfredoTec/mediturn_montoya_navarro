package com.tecsup.mediturn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.repository.AppointmentRepository
import com.tecsup.mediturn.data.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val upcomingAppointments: List<Appointment> = emptyList(),
    val featuredDoctors: List<Doctor> = emptyList()
)

class HomeViewModel(
    private val doctorRepository: DoctorRepository = DoctorRepository(),
    private val appointmentRepository: AppointmentRepository = AppointmentRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val appointments = appointmentRepository.getAllAppointments()
            val doctors = doctorRepository.getAllDoctors().take(3)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                upcomingAppointments = appointments,
                featuredDoctors = doctors
            )
        }
    }
}