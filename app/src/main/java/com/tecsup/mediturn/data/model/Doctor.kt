package com.tecsup.mediturn.data.model


data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviewCount: Int,
    val experience: String,
    val nextAvailableSlot: String,
    val pricePerConsultation: Double,
    val imageUrl: String = "",
    val isTelehealthAvailable: Boolean = false,
    val location: String,
    val about: String = "",
    val availableTimeSlots: List<TimeSlot> = emptyList()
)