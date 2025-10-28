package com.tecsup.mediturn.data.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entidad Room que representa a los doctores en la base de datos
 * Almacena información del doctor, especialidad, disponibilidad y precios
 */
@Entity(tableName = "doctors")
data class Doctor(
    @PrimaryKey
    val id: String,
    val name: String,
    val specialty: Specialty,
    val experience: String,
    val nextAvailableSlot: Date,
    val pricePerConsultation: Double,
    @DrawableRes
    val imageResId: Int = 0,
    val isTelehealthAvailable: Boolean = false,
    val location: String,
    val about: String = "",
    val availableTimeSlots: List<TimeSlot> = emptyList()
)