package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.local.dao.AppointmentDao
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Date

/**
 * Repositorio para operaciones con citas médicas
 * Usa Room como fuente de datos persistente
 * Proporciona métodos síncronos y asíncronos para compatibilidad
 */
class AppointmentRepository(private val appointmentDao: AppointmentDao) {

    /**
     * Obtiene todas las citas de forma síncrona
     * Usado por código legacy que espera List<Appointment>
     */
    fun getAllAppointments(): List<Appointment> {
        return runBlocking {
            appointmentDao.getAllAppointments().first()
        }
    }

    /**
     * Obtiene todas las citas como Flow para observar cambios
     * Recomendado para uso en ViewModels con StateFlow
     */
    fun getAllAppointmentsFlow(): Flow<List<Appointment>> {
        return appointmentDao.getAllAppointments()
    }

    /**
     * Obtiene citas próximas (CONFIRMED o PENDING) como Flow
     */
    fun getUpcomingAppointmentsFlow(): Flow<List<Appointment>> {
        return appointmentDao.getUpcomingAppointments()
    }

    /**
     * Obtiene citas pasadas (COMPLETED o CANCELLED) como Flow
     */
    fun getPastAppointmentsFlow(): Flow<List<Appointment>> {
        return appointmentDao.getPastAppointments()
    }

    /**
     * Busca una cita por ID de forma síncrona
     */
    fun getAppointmentById(id: String): Appointment? {
        return runBlocking {
            appointmentDao.getAppointmentById(id)
        }
    }

    /**
     * Agrega una nueva cita a la base de datos
     */
    fun addAppointment(appointment: Appointment) {
        runBlocking {
            appointmentDao.insertAppointment(appointment)
        }
    }

    /**
     * Cancela una cita cambiando su estado a CANCELLED
     */
    fun cancelAppointment(id: String) {
        runBlocking {
            appointmentDao.updateAppointmentStatus(id, AppointmentStatus.CANCELLED)
        }
    }

    /**
     * Reprograma una cita cambiando su fecha
     */
    fun rescheduleAppointment(id: String, newDate: Date) {
        runBlocking {
            appointmentDao.updateAppointmentDate(id, newDate)
        }
    }
}
