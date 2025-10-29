package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.DataSourceConfig
import com.tecsup.mediturn.data.local.dao.AppointmentDao
import com.tecsup.mediturn.data.model.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.util.Date

/**
 * Repositorio unificado para citas que puede usar Room o Retrofit
 */
class UnifiedAppointmentRepository(
    private val appointmentDao: AppointmentDao? = null  // Opcional, solo para Room
) {
    private val localRepository: AppointmentRepository? by lazy {
        appointmentDao?.let { AppointmentRepository(it) }
    }

    private val remoteRepository: RemoteAppointmentRepository by lazy {
        RemoteAppointmentRepository()
    }

    /**
     * Obtiene todas las citas de forma síncrona
     */
    fun getAllAppointments(): List<Appointment> {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.getAllAppointments() ?: emptyList()
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                runBlocking {
                    var appointments = emptyList<Appointment>()
                    remoteRepository.getAllAppointmentsFromApi().collect {
                        appointments = it
                    }
                    appointments
                }
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                runBlocking {
                    var appointments = emptyList<Appointment>()
                    remoteRepository.getAllAppointmentsFromApi().collect { remoteAppts ->
                        appointments = if (remoteAppts.isNotEmpty()) {
                            remoteAppts
                        } else {
                            localRepository?.getAllAppointments() ?: emptyList()
                        }
                    }
                    appointments
                }
            }
        }
    }

    /**
     * Obtiene todas las citas como Flow
     */
    fun getAllAppointmentsFlow(): Flow<List<Appointment>> {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.getAllAppointmentsFlow() ?: flow { emit(emptyList()) }
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                remoteRepository.getAllAppointmentsFromApi()
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                flow {
                    remoteRepository.getAllAppointmentsFromApi().collect { remoteAppts ->
                        if (remoteAppts.isNotEmpty()) {
                            emit(remoteAppts)
                        } else {
                            localRepository?.getAllAppointmentsFlow()?.collect { emit(it) }
                        }
                    }
                }
            }
        }
    }

    /**
     * Obtiene citas próximas (CONFIRMED o PENDING)
     */
    fun getUpcomingAppointmentsFlow(): Flow<List<Appointment>> {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.getUpcomingAppointmentsFlow() ?: flow { emit(emptyList()) }
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                remoteRepository.getUpcomingAppointmentsFromApi()
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                flow {
                    remoteRepository.getUpcomingAppointmentsFromApi().collect { remoteAppts ->
                        if (remoteAppts.isNotEmpty()) {
                            emit(remoteAppts)
                        } else {
                            localRepository?.getUpcomingAppointmentsFlow()?.collect { emit(it) }
                        }
                    }
                }
            }
        }
    }

    /**
     * Obtiene citas pasadas (COMPLETED o CANCELLED)
     */
    fun getPastAppointmentsFlow(): Flow<List<Appointment>> {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.getPastAppointmentsFlow() ?: flow { emit(emptyList()) }
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                remoteRepository.getPastAppointmentsFromApi()
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                flow {
                    remoteRepository.getPastAppointmentsFromApi().collect { remoteAppts ->
                        if (remoteAppts.isNotEmpty()) {
                            emit(remoteAppts)
                        } else {
                            localRepository?.getPastAppointmentsFlow()?.collect { emit(it) }
                        }
                    }
                }
            }
        }
    }

    /**
     * Obtiene cita por ID (síncrono)
     */
    fun getAppointmentById(id: String): Appointment? {
        return when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.getAppointmentById(id)
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                runBlocking {
                    remoteRepository.getAppointmentByIdFromApi(id)
                }
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                runBlocking {
                    remoteRepository.getAppointmentByIdFromApi(id)
                        ?: localRepository?.getAppointmentById(id)
                }
            }
        }
    }

    /**
     * Agrega una nueva cita
     */
    fun addAppointment(appointment: Appointment) {
        when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.addAppointment(appointment)
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                runBlocking {
                    // TODO: Necesitas obtener el patientId del contexto
                    remoteRepository.createAppointment(appointment, "PATIENT_ID_PLACEHOLDER")
                }
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                // Guardar en ambos
                localRepository?.addAppointment(appointment)
                runBlocking {
                    remoteRepository.createAppointment(appointment, "PATIENT_ID_PLACEHOLDER")
                }
            }
        }
    }

    /**
     * Cancela una cita
     */
    fun cancelAppointment(id: String) {
        when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.cancelAppointment(id)
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                runBlocking {
                    remoteRepository.cancelAppointment(id)
                }
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                localRepository?.cancelAppointment(id)
                runBlocking {
                    remoteRepository.cancelAppointment(id)
                }
            }
        }
    }

    /**
     * Reprograma una cita
     */
    fun rescheduleAppointment(id: String, newDate: Date) {
        when (DataSourceConfig.currentDataSource) {
            DataSourceConfig.DataSourceType.LOCAL_ROOM -> {
                localRepository?.rescheduleAppointment(id, newDate)
            }
            DataSourceConfig.DataSourceType.REMOTE_API -> {
                // TODO: Implementar con Retrofit
            }
            DataSourceConfig.DataSourceType.HYBRID -> {
                localRepository?.rescheduleAppointment(id, newDate)
                // TODO: Sincronizar con remoto
            }
        }
    }
}
