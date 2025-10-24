package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.local.SampleData
import com.tecsup.mediturn.data.model.Doctor

class DoctorRepository {

    fun getAllDoctors(): List<Doctor> {
        return SampleData.sampleDoctors
    }

    fun getDoctorById(id: String): Doctor? {
        return SampleData.sampleDoctors.find { it.id == id }
    }

    fun searchDoctors(query: String): List<Doctor> {
        if (query.isBlank()) return getAllDoctors()
        return SampleData.sampleDoctors.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.specialty.contains(query, ignoreCase = true)
        }
    }

    fun getDoctorsBySpecialty(specialty: String): List<Doctor> {
        return SampleData.sampleDoctors.filter { it.specialty == specialty }
    }
}