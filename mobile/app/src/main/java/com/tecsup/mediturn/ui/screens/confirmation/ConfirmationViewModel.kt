package com.tecsup.mediturn.ui.screens.confirmation

import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.repository.AppointmentRepository
import com.tecsup.mediturn.data.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ConfirmationUiState(
    val appointment: Appointment? = null,
    val isLoading: Boolean = true
)

class ConfirmationViewModel(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmationUiState())
    val uiState: StateFlow<ConfirmationUiState> = _uiState.asStateFlow()

    fun loadAppointment(appointmentId: String) {
        val appointment = appointmentRepository.getAppointmentById(appointmentId)
        _uiState.value = _uiState.value.copy(
            appointment = appointment,
            isLoading = false
        )
    }
}