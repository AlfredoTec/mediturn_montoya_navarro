package com.tecsup.mediturn.data.model

import java.util.Date

// Clase que representa las citas médicas
data class Appointment(
    val id: String,
    val doctor: Doctor,
    val date: Date,
    val consultationType: ConsultationType,
    val reason: String = "",
    val status: AppointmentStatus = AppointmentStatus.CONFIRMED
)

// Tipo de consulta (presencial o virtual)
enum class ConsultationType {
    IN_PERSON,
    TELEHEALTH
}

// Estado de la cita médica
enum class AppointmentStatus {
    CONFIRMED,
    PENDING,
    CANCELLED,
    COMPLETED
}