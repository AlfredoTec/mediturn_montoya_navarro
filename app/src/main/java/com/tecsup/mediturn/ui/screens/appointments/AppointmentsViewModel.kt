package com.tecsup.mediturn.ui.screens.appointments

import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.repository.AppointmentRepository
import com.tecsup.mediturn.data.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppointmentsUiState(
    val upcomingAppointments: List<Appointment> = emptyList(),
    val pastAppointments: List<Appointment> = emptyList(),
    val selectedTab: Int = 0, // 0 = Próximas, 1 = Pasadas
    val isLoading: Boolean = false
)

class AppointmentsViewModel : ViewModel() {

    private val appointmentRepository = AppointmentRepository()

    private val _uiState = MutableStateFlow(AppointmentsUiState())
    val uiState: StateFlow<AppointmentsUiState> = _uiState.asStateFlow()

    init {
        loadAppointments()
    }

    fun loadAppointments() {
        _uiState.value = _uiState.value.copy(
            upcomingAppointments = appointmentRepository.getUpcomingAppointments(),
            pastAppointments = appointmentRepository.getPastAppointments()
        )
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tabIndex)
    }

    fun cancelAppointment(appointmentId: String) {
        appointmentRepository.cancelAppointment(appointmentId)
        loadAppointments() // Recargar lista
    }

    fun rescheduleAppointment(appointmentId: String, newDate: String, newTime: String) {
        appointmentRepository.rescheduleAppointment(appointmentId, newDate, newTime)
        loadAppointments() // Recargar lista
    }
}