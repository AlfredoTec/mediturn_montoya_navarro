package com.tecsup.mediturn.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.tecsup.mediturn.data.model.TimeSlot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * DTO para TimeSlot desde la API Django
 */
data class TimeSlotDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("datetime")
    val datetime: String,  // Formato ISO 8601

    @SerializedName("is_available")
    val isAvailable: Boolean,

    @SerializedName("doctor")
    val doctorId: String? = null
) {
    /**
     * Convierte el DTO al modelo de dominio
     */
    fun toDomain(): TimeSlot {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        return TimeSlot(
            id = id,
            dateTime = try {
                dateFormat.parse(datetime) ?: Date()
            } catch (e: Exception) {
                Date()
            },
            isAvailable = isAvailable
        )
    }
}
