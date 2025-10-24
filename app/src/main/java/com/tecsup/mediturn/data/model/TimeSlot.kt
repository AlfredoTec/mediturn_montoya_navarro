package com.tecsup.mediturn.data.model

data class TimeSlot(
    val id: String,
    val time: String,
    val date: String,
    val isAvailable: Boolean = true
)