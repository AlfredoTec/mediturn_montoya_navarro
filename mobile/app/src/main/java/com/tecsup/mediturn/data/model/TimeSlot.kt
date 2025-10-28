package com.tecsup.mediturn.data.model

import java.util.Date

// Espacios de atención que tienen los médicos
data class TimeSlot(
    val id: String,
    val dateTime: Date,
    val isAvailable: Boolean = true
)