package com.tecsup.mediturn.data.local.dao

import androidx.room.*
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.model.Specialty
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {

    @Query("SELECT * FROM doctors")
    fun getAllDoctors(): Flow<List<Doctor>>

    @Query("SELECT * FROM doctors WHERE id = :doctorId")
    suspend fun getDoctorById(doctorId: String): Doctor?

    @Query("SELECT * FROM doctors WHERE specialty = :specialty")
    fun getDoctorsBySpecialty(specialty: Specialty): Flow<List<Doctor>>

    @Query("SELECT * FROM doctors WHERE name LIKE '%' || :query || '%'")
    fun searchDoctors(query: String): Flow<List<Doctor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDoctors(doctors: List<Doctor>)

    @Query("DELETE FROM doctors")
    suspend fun deleteAllDoctors()
}
