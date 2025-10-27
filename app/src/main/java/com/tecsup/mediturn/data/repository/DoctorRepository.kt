package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.local.dao.DoctorDao
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.model.Specialty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * Repositorio para operaciones con doctores
 * Usa Room como fuente de datos persistente
 * Proporciona métodos síncronos y asíncronos para compatibilidad
 */
class DoctorRepository(private val doctorDao: DoctorDao) {

    /**
     * Obtiene todos los doctores de forma síncrona
     * Usado por código legacy que espera List<Doctor>
     */
    fun getAllDoctors(): List<Doctor> {
        return runBlocking {
            doctorDao.getAllDoctors().first()
        }
    }

    /**
     * Obtiene todos los doctores como Flow para observar cambios
     * Recomendado para uso en ViewModels con StateFlow
     */
    fun getAllDoctorsFlow(): Flow<List<Doctor>> {
        return doctorDao.getAllDoctors()
    }

    /**
     * Obtiene un doctor por ID de forma síncrona
     * Usado por ViewModels que esperan Doctor? directamente
     */
    fun getDoctorById(id: String): Doctor? {
        return runBlocking {
            doctorDao.getDoctorById(id)
        }
    }


    /**
     * Búsqueda avanzada de doctores con múltiples filtros
     * Usa Flow de Room para observar cambios en la base de datos
     */
    fun searchDoctorsAdvanced(
        query: String = "",
        specialty: Specialty? = null,
        city: String? = null,
        teleconsultation: Boolean? = null
    ): Flow<List<Doctor>> {
        // Obtiene todos los doctores de Room y aplica filtros en memoria
        return doctorDao.getAllDoctors().map { doctors ->
            var results = doctors

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

            results
        }
    }
}