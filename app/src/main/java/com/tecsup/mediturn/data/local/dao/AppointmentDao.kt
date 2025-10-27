package com.tecsup.mediturn.data.local.dao

import androidx.room.*
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * DAO para operaciones de base de datos con citas médicas
 * Proporciona métodos para crear, leer, actualizar y eliminar citas
 */
@Dao
interface AppointmentDao {

    // Obtiene todas las citas como Flow para observar cambios en tiempo real
    @Query("SELECT * FROM appointments ORDER BY date DESC")
    fun getAllAppointments(): Flow<List<Appointment>>

    // Busca una cita específica por ID
    @Query("SELECT * FROM appointments WHERE id = :appointmentId")
    suspend fun getAppointmentById(appointmentId: String): Appointment?

    // Obtiene citas por estado (CONFIRMED, PENDING, etc.)
    @Query("SELECT * FROM appointments WHERE status = :status ORDER BY date ASC")
    fun getAppointmentsByStatus(status: AppointmentStatus): Flow<List<Appointment>>

    // Obtiene citas próximas (CONFIRMED o PENDING)
    @Query("SELECT * FROM appointments WHERE status IN ('CONFIRMED', 'PENDING') ORDER BY date ASC")
    fun getUpcomingAppointments(): Flow<List<Appointment>>

    // Obtiene citas pasadas (COMPLETED o CANCELLED)
    @Query("SELECT * FROM appointments WHERE status IN ('COMPLETED', 'CANCELLED') ORDER BY date DESC")
    fun getPastAppointments(): Flow<List<Appointment>>

    // Inserta una nueva cita
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    // Inserta múltiples citas (útil para datos de muestra)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAppointments(appointments: List<Appointment>)

    // Actualiza una cita existente
    @Update
    suspend fun updateAppointment(appointment: Appointment)

    // Actualiza solo el estado de una cita
    @Query("UPDATE appointments SET status = :status WHERE id = :appointmentId")
    suspend fun updateAppointmentStatus(appointmentId: String, status: AppointmentStatus)

    // Actualiza solo la fecha de una cita (para reprogramar)
    @Query("UPDATE appointments SET date = :newDate WHERE id = :appointmentId")
    suspend fun updateAppointmentDate(appointmentId: String, newDate: Date)

    // Elimina una cita específica
    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    // Elimina todas las citas
    @Query("DELETE FROM appointments")
    suspend fun deleteAllAppointments()
}
