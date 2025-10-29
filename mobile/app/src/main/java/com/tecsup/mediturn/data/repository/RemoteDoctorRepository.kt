package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.model.Specialty
import com.tecsup.mediturn.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repositorio remoto para operaciones con doctores desde la API Django
 * Usa Retrofit para comunicarse con el backend
 */
class RemoteDoctorRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Obtiene todos los doctores desde la API
     * @return Flow con lista de doctores o lista vacía si hay error
     */
    fun getAllDoctorsFromApi(): Flow<List<Doctor>> = flow {
        try {
            val response = apiService.getDoctors()
            if (response.isSuccessful && response.body() != null) {
                val doctors = response.body()!!.map { it.toDomain() }
                emit(doctors)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    /**
     * Obtiene un doctor específico por ID desde la API
     * @param doctorId ID del doctor
     * @return Doctor o null si no se encuentra o hay error
     */
    suspend fun getDoctorByIdFromApi(doctorId: String): Doctor? {
        return try {
            val response = apiService.getDoctorById(doctorId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.toDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Búsqueda de doctores con filtros
     * Como la API Django no tiene endpoints personalizados de búsqueda,
     * primero obtenemos todos y luego filtramos en memoria
     */
    fun searchDoctors(
        query: String = "",
        specialty: Specialty? = null,
        city: String? = null,
        teleconsultation: Boolean? = null
    ): Flow<List<Doctor>> = flow {
        try {
            val response = apiService.getDoctors()
            if (response.isSuccessful && response.body() != null) {
                var doctors = response.body()!!.map { it.toDomain() }

                // Aplicar filtros
                if (query.isNotBlank()) {
                    doctors = doctors.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }

                if (specialty != null) {
                    doctors = doctors.filter { it.specialty == specialty }
                }

                if (city != null) {
                    doctors = doctors.filter {
                        it.location.contains(city, ignoreCase = true)
                    }
                }

                if (teleconsultation == true) {
                    doctors = doctors.filter { it.isTelehealthAvailable }
                }

                emit(doctors)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    /**
     * Manejo de errores estructurado
     * Envuelve los resultados de la API en un Result
     */
    suspend fun getDoctorsResult(): Result<List<Doctor>> {
        return try {
            val response = apiService.getDoctors()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { it.toDomain() })
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
