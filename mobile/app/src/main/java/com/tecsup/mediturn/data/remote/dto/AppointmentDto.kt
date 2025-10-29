package com.tecsup.mediturn.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import com.tecsup.mediturn.data.model.ConsultationType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * DTO para Appointment desde la API Django
 */
data class AppointmentDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("doctor")
    val doctor: DoctorDto? = null,  // Para GET (lectura)

    @SerializedName("patient")
    val patient: PatientDto? = null,  // Para GET (lectura)

    @SerializedName("doctor_id")
    val doctorId: String? = null,  // Para POST/PUT (escritura)

    @SerializedName("patient_id")
    val patientId: String? = null,  // Para POST/PUT (escritura)

    @SerializedName("date")
    val date: String,  // Formato ISO 8601

    @SerializedName("consultation_type")
    val consultationType: String,

    @SerializedName("reason")
    val reason: String,

    @SerializedName("status")
    val status: String
) {
    /**
     * Convierte el DTO al modelo de dominio
     */
    fun toDomain(): Appointment {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        return Appointment(
            id = id,
            doctor = doctor?.toDomain() ?: throw IllegalStateException("Doctor no puede ser null"),
            date = try {
                dateFormat.parse(date) ?: Date()
            } catch (e: Exception) {
                Date()
            },
            consultationType = try {
                ConsultationType.valueOf(consultationType)
            } catch (e: Exception) {
                ConsultationType.IN_PERSON
            },
            reason = reason,
            status = try {
                AppointmentStatus.valueOf(status)
            } catch (e: Exception) {
                AppointmentStatus.PENDING
            }
        )
    }

    companion object {
        /**
         * Crea un DTO para crear una cita en el servidor
         */
        fun fromDomain(appointment: Appointment, patientId: String): AppointmentDto {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

            return AppointmentDto(
                id = appointment.id,
                doctorId = appointment.doctor.id,
                patientId = patientId,
                date = dateFormat.format(appointment.date),
                consultationType = appointment.consultationType.name,
                reason = appointment.reason,
                status = appointment.status.name
            )
        }
    }
}
