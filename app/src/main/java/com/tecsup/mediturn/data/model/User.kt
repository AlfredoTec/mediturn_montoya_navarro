package com.tecsup.mediturn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "user")

data class User(
    //@PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val direccion: String
)