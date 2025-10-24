package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.local.SampleData
import com.tecsup.mediturn.data.model.Appointment

class AppointmentRepository {

    private val appointments = mutableListOf<Appointment>().apply {
        addAll(SampleData.sampleAppointments)
    }

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
            appointments[index] = appointments[index].copy(
                status = com.tecsup.mediturn.data.model.AppointmentStatus.CANCELLED
            )
        }
    }
}