package com.tecsup.mediturn.data.model

import java.util.Date

// Clase que representa a los pacientes
data class Patient(
    val id: String,
    val name: String,
    val email: String,
    val phone: String = "",
    val dateOfBirth: Date
)