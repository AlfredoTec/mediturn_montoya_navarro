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
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val reasonError: String? = null,
    val timeSlotError: String? = null,
    val consultationTypeError: String? = null
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
            selectedDate = timeSlot.dateTime.toString(),
        )
    }

    fun onConsultationTypeChanged(type: ConsultationType) {
        _uiState.value = _uiState.value.copy(consultationType = type)
    }

    fun onReasonChanged(reason: String) {
        _uiState.value = _uiState.value.copy(
            reason = reason,
            reasonError = validateReason(reason)
        )
    }

    private fun validateReason(reason: String): String? {
        return when {
            reason.length > 500 -> "El motivo no puede exceder 500 caracteres"
            reason.trim().length < 5 && reason.isNotBlank() -> "El motivo debe tener al menos 5 caracteres"
            else -> null
        }
    }

    private fun validateTimeSlot(): String? {
        val timeSlot = _uiState.value.selectedTimeSlot
        return when {
            timeSlot == null -> "Debe seleccionar una fecha y hora"
            !timeSlot.isAvailable -> "Este horario ya no está disponible"
            else -> null
        }
    }

    private fun validateConsultationType(): String? {
        val doctor = _uiState.value.doctor
        val consultationType = _uiState.value.consultationType

        return when {
            doctor == null -> "Error: Doctor no encontrado"
            consultationType == ConsultationType.TELEHEALTH && !doctor.isTelehealthAvailable ->
                "Este doctor no ofrece teleconsulta"
            else -> null
        }
    }

    private fun validateAllFields(): Boolean {
        val timeSlotError = validateTimeSlot()
        val consultationTypeError = validateConsultationType()
        val reasonError = if (_uiState.value.reason.isNotBlank()) validateReason(_uiState.value.reason) else null

        _uiState.value = _uiState.value.copy(
            timeSlotError = timeSlotError,
            consultationTypeError = consultationTypeError,
            reasonError = reasonError,
            errorMessage = null
        )

        return timeSlotError == null && consultationTypeError == null && reasonError == null
    }

    fun bookAppointment(): String {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        if (!validateAllFields()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Por favor, corrija los errores antes de continuar"
            )
            return ""
        }

        val doctor = _uiState.value.doctor
        val timeSlot = _uiState.value.selectedTimeSlot

        if (doctor == null || timeSlot == null) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Error inesperado. Por favor, intente nuevamente."
            )
            return ""
        }

        try {
            val newAppointment = Appointment(
                id = "apt_${System.currentTimeMillis()}",
                doctor = doctor,
                date = timeSlot.dateTime,
                consultationType = _uiState.value.consultationType,
                reason = _uiState.value.reason.trim(),
                status = AppointmentStatus.CONFIRMED
            )

            appointmentRepository.addAppointment(newAppointment)
            _uiState.value = _uiState.value.copy(isLoading = false)
            return newAppointment.id
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Error al crear la cita: ${e.message}"
            )
            return ""
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}