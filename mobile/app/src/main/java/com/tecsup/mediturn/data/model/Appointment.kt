package com.tecsup.mediturn.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entidad Room que representa las citas médicas en la base de datos
 * Usa @Embedded para incluir toda la información del doctor en la misma tabla
 * Almacena fecha, tipo de consulta, motivo y estado de la cita
 */
@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey
    val id: String,
    @Embedded(prefix = "doctor_")
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