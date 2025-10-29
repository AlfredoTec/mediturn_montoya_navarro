package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.DataSourceConfig
import com.tecsup.mediturn.data.local.dao.DoctorDao
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.model.Specialty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * Repositorio unificado que puede usar Room o Retrofit según la configuración
 * Permite cambiar la fuente de datos sin modificar los ViewModels
 */
class UnifiedDoctorRepository(
    private val doctorDao: DoctorDao? = null  // Opcional, solo necesario para Room
) {
    // Instancias lazy de los repositorios específicos
    private val localRepository: DoctorRepository? by lazy {
        doctorDao?.let { DoctorRepository(it) }
    }

    private val remoteRepository: RemoteDoctorRepository by lazy {
        RemoteDoctorRepository()
    }

    /**
     * Obtiene todos los doctores según la fuente configurada
     * @return Flow con lista de doctores
     */
    fun getAllDoctorsFlow(): Flow<List<Doctor>> {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.getAllDoctorsFlow() ?: flow { emit(emptyList()) }
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                remoteRepository.getAllDoctorsFromApi()
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                // TODO: Implementar estrategia híbrida
                // Por ahora usa remoto con fallback a local
                flow {
                    remoteRepository.getAllDoctorsFromApi().collect { remoteDoctors ->
                        if (remoteDoctors.isNotEmpty()) {
                            emit(remoteDoctors)
                        } else {
                            localRepository?.getAllDoctorsFlow()?.collect { emit(it) }
                        }
                    }
                }
            }
        }
    }

    /**
     * Obtiene todos los doctores de forma síncrona
     * @return Lista de doctores
     */
    fun getAllDoctors(): List<Doctor> {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.getAllDoctors() ?: emptyList()
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                runBlocking {
                    var doctors = emptyList<Doctor>()
                    remoteRepository.getAllDoctorsFromApi().collect {
                        doctors = it
                    }
                    doctors
                }
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                runBlocking {
                    var doctors = emptyList<Doctor>()
                    remoteRepository.getAllDoctorsFromApi().collect { remoteDoctors ->
                        doctors = if (remoteDoctors.isNotEmpty()) {
                            remoteDoctors
                        } else {
                            localRepository?.getAllDoctors() ?: emptyList()
                        }
                    }
                    doctors
                }
            }
        }
    }

    /**
     * Obtiene un doctor por ID
     * @param id ID del doctor
     * @return Doctor o null si no se encuentra
     */
    fun getDoctorById(id: String): Doctor? {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.getDoctorById(id)
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                runBlocking {
                    remoteRepository.getDoctorByIdFromApi(id)
                }
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                runBlocking {
                    remoteRepository.getDoctorByIdFromApi(id)
                        ?: localRepository?.getDoctorById(id)
                }
            }
        }
    }

    /**
     * Búsqueda avanzada de doctores con filtros
     * @param query Texto de búsqueda
     * @param specialty Especialidad a filtrar
     * @param city Ciudad a filtrar
     * @param teleconsultation Si solo mostrar con teleconsulta
     * @return Flow con lista de doctores filtrados
     */
    fun searchDoctorsAdvanced(
        query: String = "",
        specialty: Specialty? = null,
        city: String? = null,
        teleconsultation: Boolean? = null
    ): Flow<List<Doctor>> {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.searchDoctorsAdvanced(query, specialty, city, teleconsultation)
                    ?: flow { emit(emptyList()) }
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                remoteRepository.searchDoctors(query, specialty, city, teleconsultation)
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                flow {
                    remoteRepository.searchDoctors(query, specialty, city, teleconsultation)
                        .collect { remoteDoctors ->
                            if (remoteDoctors.isNotEmpty()) {
                                emit(remoteDoctors)
                            } else {
                                localRepository?.searchDoctorsAdvanced(
                                    query,
                                    specialty,
                                    city,
                                    teleconsultation
                                )?.collect { emit(it) }
                            }
                        }
                }
            }
        }
    }

    /**
     * Obtiene el nombre de la fuente de datos actual para mostrar en UI
     */
    fun getCurrentDataSourceName(): String {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> "Room (Local)"
            DataSourceConfig.DataSourceType.REMOTE_API -> "Retrofit (API)"
            DataSourceConfig.DataSourceType.HYBRID -> "Híbrido"
        }
    }
}
