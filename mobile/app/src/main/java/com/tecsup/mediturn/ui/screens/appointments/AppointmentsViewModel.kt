package com.tecsup.mediturn.ui.screens.appointments

import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.repository.AppointmentRepository
import com.tecsup.mediturn.data.repository.UnifiedAppointmentRepository
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppointmentsUiState(
    val selectedTab: Int = 0,
    val upcomingAppointments: List<Appointment> = emptyList(),
    val pastAppointments: List<Appointment> = emptyList()
)

class AppointmentsViewModel(
    private val appointmentRepository: AppointmentRepository? = null,  // Legacy
    private val unifiedRepository: UnifiedAppointmentRepository? = null  // Nuevo
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentsUiState())
    val uiState: StateFlow<AppointmentsUiState> = _uiState.asStateFlow()

    init {
        loadAppointments()
    }

    private fun loadAppointments() {
        val all: List<Appointment> = when {
            unifiedRepository != null -> unifiedRepository.getAllAppointments()
            appointmentRepository != null -> appointmentRepository.getAllAppointments()
            else -> emptyList()
        }

        val upcoming: List<Appointment> = all.filter { appointment: Appointment ->
            appointment.status == AppointmentStatus.CONFIRMED ||
                    appointment.status == AppointmentStatus.PENDING
        }

        val past: List<Appointment> = all.filter { appointment: Appointment ->
            appointment.status == AppointmentStatus.COMPLETED ||
                    appointment.status == AppointmentStatus.CANCELLED
        }

        _uiState.value = _uiState.value.copy(
            upcomingAppointments = upcoming,
            pastAppointments = past
        )
    }

    fun onTabSelected(tab: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun cancelAppointment(appointmentId: String) {
        when {
            unifiedRepository != null -> unifiedRepository.cancelAppointment(appointmentId)
            appointmentRepository != null -> appointmentRepository.cancelAppointment(appointmentId)
        }
        loadAppointments()
    }

    fun rescheduleAppointment(appointmentId: String, newDate: java.util.Date) {
        when {
            unifiedRepository != null -> unifiedRepository.rescheduleAppointment(appointmentId, newDate)
            appointmentRepository != null -> appointmentRepository.rescheduleAppointment(appointmentId, newDate)
        }
        loadAppointments()
    }
}
