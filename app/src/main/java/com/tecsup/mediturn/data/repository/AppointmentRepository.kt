package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import com.tecsup.mediturn.data.model.SampleData
import java.util.Date

class AppointmentRepository {

    // Se obtiene todas las citas de la SampleData
    private val appointments = SampleData.sampleAppointments.toMutableList()

    // Retorna todas las citas
    fun getAllAppointments(): List<Appointment> {
        return appointments.toList()
    }

    // Busca una cita por id
    fun getAppointmentById(id: String): Appointment? {
        return appointments.find { it.id == id }
    }

    // Agrega una cita a la SampleData (cita temporal)
    fun addAppointment(appointment: Appointment) {
        appointments.add(appointment)
    }

    // Cancelar una cita por id
    fun cancelAppointment(id: String) {
        // Va a obtener el index de la cita (va a buscar por el indice), si no la encuentra retorna -1
        val index = appointments.indexOfFirst { it.id == id }

        // Si encuentra la cita, va a cambiar el estado a CANCELLED
        if (index != -1) {
            appointments[index] = appointments[index].copy(status = AppointmentStatus.CANCELLED)
        }
    }


    fun rescheduleAppointment(id: String, newDate: Date) {
        // Va a obtener el index de la cita (va a buscar por el indice), si no la encuentra retorna -1
        val index = appointments.indexOfFirst { it.id == id }

        // Si encuentra la cita, va a cambiar la fecha a la nueva fecha
        if (index != -1) {
            appointments[index] = appointments[index].copy(
                date = newDate
            )
        }
    }
}