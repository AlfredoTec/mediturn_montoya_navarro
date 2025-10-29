package com.tecsup.mediturn.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.tecsup.mediturn.data.model.Patient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * DTO para Patient desde la API Django
 */
data class PatientDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("date_of_birth")
    val dateOfBirth: String  // Formato: YYYY-MM-DD
) {
    /**
     * Convierte el DTO al modelo de dominio
     */
    fun toDomain(): Patient {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return Patient(
            id = id,
            name = name,
            email = email,
            phone = phone,
            dateOfBirth = try {
                dateFormat.parse(dateOfBirth) ?: Date()
            } catch (e: Exception) {
                Date()
            }
        )
    }

    companion object {
        /**
         * Crea un DTO desde el modelo de dominio para enviar al servidor
         */
        fun fromDomain(patient: Patient): PatientDto {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            return PatientDto(
                id = patient.id,
                name = patient.name,
                email = patient.email,
                phone = patient.phone,
                dateOfBirth = dateFormat.format(patient.dateOfBirth)
            )
        }
    }
}
