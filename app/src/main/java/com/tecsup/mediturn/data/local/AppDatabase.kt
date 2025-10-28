package com.tecsup.mediturn.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tecsup.mediturn.data.local.dao.AppointmentDao
import com.tecsup.mediturn.data.local.dao.DoctorDao
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.Doctor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de datos Room principal de la aplicación MediTurn
 * Contiene las tablas de doctores y citas médicas
 * Versión 1: Estructura inicial con Doctor y Appointment
 */
@Database(
    entities = [Doctor::class, Appointment::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // DAOs para acceso a datos
    abstract fun doctorDao(): DoctorDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia singleton de la base de datos
         * Usa patrón Double-Check Locking para thread-safety
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mediturn_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Callback que se ejecuta cuando se crea la base de datos
         * Inserta datos de muestra para desarrollo y testing
         */
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Inserta datos de muestra en background thread
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.doctorDao(), database.appointmentDao())
                    }
                }
            }
        }

        /**
         * Puebla la base de datos con datos de muestra de SampleData
         * Se ejecuta solo la primera vez que se crea la base de datos
         */
        private suspend fun populateDatabase(doctorDao: DoctorDao, appointmentDao: AppointmentDao) {
            // Inserta doctores de muestra
            doctorDao.insertAllDoctors(SampleData.sampleDoctors)

            // Inserta citas de muestra
            appointmentDao.insertAllAppointments(SampleData.sampleAppointments)
        }
    }
}
