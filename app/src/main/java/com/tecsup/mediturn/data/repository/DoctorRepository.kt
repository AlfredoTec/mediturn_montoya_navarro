package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.local.SampleData
import com.tecsup.mediturn.data.model.Doctor

class DoctorRepository {

    /**
     * Obtiene todos los doctores
     */
    fun getAllDoctors(): List<Doctor> {
        return SampleData.sampleDoctors
    }

    /**
     * Obtiene un doctor por ID
     */
    fun getDoctorById(id: String): Doctor? {
        return SampleData.sampleDoctors.find { it?.id == id }
    }

    /**
     * Busca doctores por especialidad
     */
    fun searchBySpecialty(specialty: String): List<Doctor> {
        if (specialty == "Todos") return getAllDoctors()
        return SampleData.sampleDoctors.filter {
            it.specialty.equals(specialty, ignoreCase = true)
        }
    }

    /**
     * Busca doctores por nombre
     */
    fun searchByName(query: String): List<Doctor> {
        if (query.isBlank()) return getAllDoctors()
        return SampleData.sampleDoctors.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    /**
     * Filtra doctores por disponibilidad de teleconsulta
     */
    fun filterByTelehealth(telehealthOnly: Boolean): List<Doctor> {
        if (!telehealthOnly) return getAllDoctors()
        return SampleData.sampleDoctors.filter { it.isTelehealthAvailable }
    }
}