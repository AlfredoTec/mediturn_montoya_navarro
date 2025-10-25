package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.local.SampleData
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus

class AppointmentRepository {

    private val appointments = mutableListOf<Appointment>().apply {
        addAll(SampleData.sampleAppointments)
    }

    /**
     * Obtiene todas las citas
     */
    fun getAllAppointments(): List<Appointment> {
        return appointments.toList()
    }

    /**
     * Obtiene citas próximas (confirmadas o pendientes)
     */
    fun getUpcomingAppointments(): List<Appointment> {
        return appointments.filter {
            it.status == AppointmentStatus.CONFIRMED ||
                    it.status == AppointmentStatus.PENDING
        }
    }

    /**
     * Obtiene citas pasadas (completadas o canceladas)
     */
    fun getPastAppointments(): List<Appointment> {
        return appointments.filter {
            it.status == AppointmentStatus.COMPLETED ||
                    it.status == AppointmentStatus.CANCELLED
        }
    }

    /**
     * Obtiene una cita por ID
     */
    fun getAppointmentById(id: String): Appointment? {
        return appointments.find { it.id == id }
    }

    /**
     * Agrega una nueva cita
     */
    fun addAppointment(appointment: Appointment) {
        appointments.add(appointment)
    }

    /**
     * Cancela una cita
     */
    fun cancelAppointment(appointmentId: String) {
        val index = appointments.indexOfFirst { it.id == appointmentId }
        if (index != -1) {
            appointments[index] = appointments[index].copy(
                status = AppointmentStatus.CANCELLED
            )
        }
    }

    /**
     * Reprograma una cita
     */
    fun rescheduleAppointment(appointmentId: String, newDate: String, newTime: String) {
        val index = appointments.indexOfFirst { it.id == appointmentId }
        if (index != -1) {
            appointments[index] = appointments[index].copy(
                date = newDate,
                time = newTime
            )
        }
    }
}