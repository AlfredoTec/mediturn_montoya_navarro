package com.tecsup.mediturn.data.model

data class User(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val direccion: String
)