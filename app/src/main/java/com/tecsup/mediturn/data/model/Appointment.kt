package com.tecsup.mediturn.data.model

data class Appointment(
    val id: String,
    val doctor: Doctor,
    val date: String,
    val time: String,
    val consultationType: ConsultationType,
    val reason: String = "",
    val status: AppointmentStatus = AppointmentStatus.CONFIRMED
)

enum class ConsultationType {
    IN_PERSON,
    TELEHEALTH
}

enum class AppointmentStatus {
    CONFIRMED,
    PENDING,
    CANCELLED,
    COMPLETED
}