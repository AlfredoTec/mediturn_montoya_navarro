package com.tecsup.mediturn.data.model

data class Patient(
    val id: String,
    val name: String,
    val email: String,
    val phone: String = "",
    val dateOfBirth: String = ""
)