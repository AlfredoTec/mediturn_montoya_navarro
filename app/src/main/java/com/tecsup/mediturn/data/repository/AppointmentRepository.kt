package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import com.tecsup.mediturn.data.local.SampleData

class AppointmentRepository {
    private val appointments = SampleData.sampleAppointments.toMutableList()

    fun getAllAppointments(): List<Appointment> {
        return appointments.toList()
    }

    fun getAppointmentById(id: String): Appointment? {
        return appointments.find { it.id == id }
    }

    fun addAppointment(appointment: Appointment) {
        appointments.add(appointment)
    }

    fun cancelAppointment(id: String) {
        val index = appointments.indexOfFirst { it.id == id }
        if (index != -1) {
            appointments[index] = appointments[index].copy(status = AppointmentStatus.CANCELLED)
        }
    }

    fun rescheduleAppointment(id: String, newDate: String, newTime: String) {
        val index = appointments.indexOfFirst { it.id == id }
        if (index != -1) {
            appointments[index] = appointments[index].copy(
                date = newDate,
                time = newTime
            )
        }
    }
}