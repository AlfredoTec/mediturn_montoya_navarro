package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.model.SampleData
import com.tecsup.mediturn.data.model.Specialty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
        return SampleData.sampleDoctors.find { it.id == id }
    }

    /**
     * Busca doctores por especialidad
     */
    fun searchBySpecialty(specialty: String): List<Doctor> {
        if (specialty == "Todos") return getAllDoctors()
        return SampleData.sampleDoctors.filter {
            it.specialty.displayName.equals(specialty, ignoreCase = true)
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

    /**
     * Búsqueda avanzada de doctores con múltiples filtros
     */
    fun searchDoctorsAdvanced(
        query: String = "",
        specialty: Specialty? = null,
        city: String? = null,
        teleconsultation: Boolean? = null
    ): Flow<List<Doctor>> = flow {
        var results = SampleData.sampleDoctors

        // Filtrar por query (nombre)
        if (query.isNotBlank()) {
            results = results.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        // Filtrar por especialidad
        if (specialty != null) {
            results = results.filter { it.specialty == specialty }
        }

        // Filtrar por ciudad
        if (city != null) {
            results = results.filter {
                it.location.contains(city, ignoreCase = true)
            }
        }

        // Filtrar por teleconsulta
        if (teleconsultation == true) {
            results = results.filter { it.isTelehealthAvailable }
        }

        emit(results)
    }
}