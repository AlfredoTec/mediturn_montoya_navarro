package com.tecsup.mediturn.data.model

import java.util.Date

// Clase que representa a los doctores
data class Doctor(
    val id: String,
    val name: String,
    val specialty: Specialty,
    val experience: String,
    val nextAvailableSlot: Date,
    val pricePerConsultation: Double,
    val imageUrl: String = "",
    val isTelehealthAvailable: Boolean = false,
    val location: String,
    val about: String = "",
    val availableTimeSlots: List<TimeSlot> = emptyList()
)