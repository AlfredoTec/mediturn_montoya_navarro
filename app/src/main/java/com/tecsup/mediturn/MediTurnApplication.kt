package com.tecsup.mediturn

import android.app.Application
import com.tecsup.mediturn.data.local.AppDatabase
import com.tecsup.mediturn.data.repository.AppointmentRepository
import com.tecsup.mediturn.data.repository.DoctorRepository

/**
 * Clase Application personalizada para inicializar componentes globales
 * Inicializa la base de datos Room y los repositorios al inicio de la app
 */
class MediTurnApplication : Application() {

    // Inicialización lazy de la base de datos
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    // Repositorio de doctores con acceso a DoctorDao
    val doctorRepository: DoctorRepository by lazy {
        DoctorRepository(database.doctorDao())
    }

    // Repositorio de citas con acceso a AppointmentDao
    val appointmentRepository: AppointmentRepository by lazy {
        AppointmentRepository(database.appointmentDao())
    }
}
