package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import com.tecsup.mediturn.data.remote.RetrofitClient
import com.tecsup.mediturn.data.remote.dto.AppointmentDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repositorio remoto para operaciones con citas desde la API Django
 * Usa Retrofit para comunicarse con el backend
 */
class RemoteAppointmentRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Obtiene todas las citas desde la API
     * @return Flow con lista de citas o lista vacía si hay error
     */
    fun getAllAppointmentsFromApi(): Flow<List<Appointment>> = flow {
        try {
            val response = apiService.getAppointments()
            if (response.isSuccessful && response.body() != null) {
                val appointments = response.body()!!.map { it.toDomain() }
                emit(appointments)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    /**
     * Obtiene citas próximas (CONFIRMED o PENDING)
     */
    fun getUpcomingAppointmentsFromApi(): Flow<List<Appointment>> = flow {
        try {
            val response = apiService.getAppointments()
            if (response.isSuccessful && response.body() != null) {
                val appointments = response.body()!!
                    .map { it.toDomain() }
                    .filter {
                        it.status == AppointmentStatus.CONFIRMED ||
                        it.status == AppointmentStatus.PENDING
                    }
                emit(appointments)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    /**
     * Obtiene citas pasadas (COMPLETED o CANCELLED)
     */
    fun getPastAppointmentsFromApi(): Flow<List<Appointment>> = flow {
        try {
            val response = apiService.getAppointments()
            if (response.isSuccessful && response.body() != null) {
                val appointments = response.body()!!
                    .map { it.toDomain() }
                    .filter {
                        it.status == AppointmentStatus.COMPLETED ||
                        it.status == AppointmentStatus.CANCELLED
                    }
                emit(appointments)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    /**
     * Obtiene una cita específica por ID desde la API
     * @param appointmentId ID de la cita
     * @return Appointment o null si no se encuentra
     */
    suspend fun getAppointmentByIdFromApi(appointmentId: String): Appointment? {
        return try {
            val response = apiService.getAppointmentById(appointmentId)
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
     * Crea una nueva cita en el servidor
     * @param appointment Cita a crear
     * @param patientId ID del paciente
     * @return Result con la cita creada o error
     */
    suspend fun createAppointment(appointment: Appointment, patientId: String): Result<Appointment> {
        return try {
            val appointmentDto = AppointmentDto.fromDomain(appointment, patientId)
            val response = apiService.createAppointment(appointmentDto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cancela una cita cambiando su estado a CANCELLED
     * @param appointmentId ID de la cita
     * @return Result con éxito o error
     */
    suspend fun cancelAppointment(appointmentId: String): Result<Appointment> {
        return try {
            val statusUpdate = mapOf("status" to "CANCELLED")
            val response = apiService.cancelAppointment(appointmentId, statusUpdate)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza una cita completa en el servidor
     * @param appointmentId ID de la cita
     * @param appointment Datos actualizados de la cita
     * @param patientId ID del paciente
     * @return Result con la cita actualizada o error
     */
    suspend fun updateAppointment(
        appointmentId: String,
        appointment: Appointment,
        patientId: String
    ): Result<Appointment> {
        return try {
            val appointmentDto = AppointmentDto.fromDomain(appointment, patientId)
            val response = apiService.updateAppointment(appointmentId, appointmentDto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina una cita del servidor
     * @param appointmentId ID de la cita
     * @return Result con éxito o error
     */
    suspend fun deleteAppointment(appointmentId: String): Result<Unit> {
        return try {
            val response = apiService.deleteAppointment(appointmentId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
