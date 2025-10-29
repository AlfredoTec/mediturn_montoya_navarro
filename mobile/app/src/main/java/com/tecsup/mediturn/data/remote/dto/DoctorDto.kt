package com.tecsup.mediturn.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.model.Specialty
import com.tecsup.mediturn.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * DTO para Doctor desde la API Django
 * Los nombres de campos coinciden con la API (snake_case)
 */
data class DoctorDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("specialty")
    val specialty: String,

    @SerializedName("experience")
    val experience: String,

    @SerializedName("next_available_slot")
    val nextAvailableSlot: String,  // Formato ISO 8601

    @SerializedName("price_per_consultation")
    val pricePerConsultation: String,  // Django devuelve Decimal como String

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("is_telehealth_available")
    val isTelehealthAvailable: Boolean,

    @SerializedName("location")
    val location: String,

    @SerializedName("about")
    val about: String,

    @SerializedName("time_slots")
    val timeSlots: List<TimeSlotDto>? = null
) {
    /**
     * Convierte el DTO de la API al modelo de dominio de la app
     */
    fun toDomain(): Doctor {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        return Doctor(
            id = id,
            name = name,
            specialty = try {
                Specialty.valueOf(specialty)
            } catch (e: Exception) {
                Specialty.GENERAL_MEDICINE
            },
            experience = experience,
            nextAvailableSlot = try {
                dateFormat.parse(nextAvailableSlot) ?: Date()
            } catch (e: Exception) {
                Date()
            },
            pricePerConsultation = pricePerConsultation.toDoubleOrNull() ?: 0.0,
            imageResId = getImageResourceForDoctor(name),
            isTelehealthAvailable = isTelehealthAvailable,
            location = location,
            about = about,
            availableTimeSlots = timeSlots?.map { it.toDomain() } ?: emptyList()
        )
    }

    /**
     * Mapea el nombre del doctor a un recurso drawable local
     * Por ahora usamos las imágenes que ya tienes en la app
     */
    private fun getImageResourceForDoctor(name: String): Int {
        return when {
            name.contains("García", ignoreCase = true) -> R.drawable.doctor_1
            name.contains("Rodríguez", ignoreCase = true) -> R.drawable.doctor_2
            name.contains("López", ignoreCase = true) -> R.drawable.doctor_3
            name.contains("Martínez", ignoreCase = true) -> R.drawable.doctor_4
            name.contains("González", ignoreCase = true) -> R.drawable.doctor_5
            name.contains("Fernández", ignoreCase = true) -> R.drawable.doctor_6
            else -> R.drawable.doctor_1
        }
    }
}
