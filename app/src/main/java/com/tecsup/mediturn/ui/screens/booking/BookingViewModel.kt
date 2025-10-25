package com.tecsup.mediturn.ui.screens.booking

import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.repository.DoctorRepository
import com.tecsup.mediturn.data.repository.AppointmentRepository
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.model.TimeSlot
import com.tecsup.mediturn.data.model.ConsultationType
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BookingUiState(
    val doctor: Doctor? = null,
    val selectedDate: String = "",
    val selectedTime: String = "",
    val selectedTimeSlot: TimeSlot? = null,
    val consultationType: ConsultationType = ConsultationType.IN_PERSON,
    val reason: String = "",
    val isLoading: Boolean = false
)

class BookingViewModel : ViewModel() {

    private val doctorRepository = DoctorRepository()
    private val appointmentRepository = AppointmentRepository()

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    fun loadDoctor(doctorId: String) {
        val doctor = doctorRepository.getDoctorById(doctorId)
        _uiState.value = _uiState.value.copy(doctor = doctor)
    }

    fun onTimeSlotSelected(timeSlot: TimeSlot) {
        _uiState.value = _uiState.value.copy(
            selectedTimeSlot = timeSlot,
            selectedDate = timeSlot.date,
            selectedTime = timeSlot.time
        )
    }

    fun onConsultationTypeChanged(type: ConsultationType) {
        _uiState.value = _uiState.value.copy(consultationType = type)
    }

    fun onReasonChanged(reason: String) {
        _uiState.value = _uiState.value.copy(reason = reason)
    }

    fun bookAppointment(): String {
        val doctor = _uiState.value.doctor ?: return ""
        val timeSlot = _uiState.value.selectedTimeSlot ?: return ""

        val newAppointment = Appointment(
            id = "apt_${System.currentTimeMillis()}",
            doctor = doctor,
            date = timeSlot.date,
            time = timeSlot.time,
            consultationType = _uiState.value.consultationType,
            reason = _uiState.value.reason,
            status = AppointmentStatus.CONFIRMED
        )

        appointmentRepository.addAppointment(newAppointment)
        return newAppointment.id
    }
}