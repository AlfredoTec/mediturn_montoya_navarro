package com.tecsup.mediturn.data.remote

import com.tecsup.mediturn.data.remote.dto.AppointmentDto
import com.tecsup.mediturn.data.remote.dto.DoctorDto
import com.tecsup.mediturn.data.remote.dto.PatientDto
import com.tecsup.mediturn.data.remote.dto.TimeSlotDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz Retrofit para consumir la API Django de MediTurn
 * Base URL: http://10.0.2.2:8000/api/
 */
interface MediTurnApiService {

    // ========== DOCTORS ==========

    /**
     * Obtiene la lista completa de doctores
     * GET /api/doctors/
     */
    @GET("doctors/")
    suspend fun getDoctors(): Response<List<DoctorDto>>

    /**
     * Obtiene el detalle de un doctor específico
     * GET /api/doctors/{id}/
     */
    @GET("doctors/{id}/")
    suspend fun getDoctorById(@Path("id") doctorId: String): Response<DoctorDto>

    /**
     * Crea un nuevo doctor (para administración)
     * POST /api/doctors/
     */
    @POST("doctors/")
    suspend fun createDoctor(@Body doctor: DoctorDto): Response<DoctorDto>


    // ========== TIME SLOTS ==========

    /**
     * Obtiene todos los time slots disponibles
     * GET /api/timeslots/
     */
    @GET("timeslots/")
    suspend fun getTimeSlots(): Response<List<TimeSlotDto>>

    /**
     * Obtiene time slots filtrados por doctor
     * GET /api/timeslots/?doctor={doctorId}
     */
    @GET("timeslots/")
    suspend fun getTimeSlotsByDoctor(@Query("doctor") doctorId: String): Response<List<TimeSlotDto>>

    /**
     * Obtiene un time slot específico
     * GET /api/timeslots/{id}/
     */
    @GET("timeslots/{id}/")
    suspend fun getTimeSlotById(@Path("id") timeSlotId: String): Response<TimeSlotDto>


    // ========== PATIENTS ==========

    /**
     * Obtiene la lista de pacientes
     * GET /api/patients/
     */
    @GET("patients/")
    suspend fun getPatients(): Response<List<PatientDto>>

    /**
     * Obtiene un paciente específico
     * GET /api/patients/{id}/
     */
    @GET("patients/{id}/")
    suspend fun getPatientById(@Path("id") patientId: String): Response<PatientDto>

    /**
     * Crea un nuevo paciente
     * POST /api/patients/
     */
    @POST("patients/")
    suspend fun createPatient(@Body patient: PatientDto): Response<PatientDto>

    /**
     * Actualiza un paciente existente
     * PUT /api/patients/{id}/
     */
    @PUT("patients/{id}/")
    suspend fun updatePatient(
        @Path("id") patientId: String,
        @Body patient: PatientDto
    ): Response<PatientDto>


    // ========== APPOINTMENTS ==========

    /**
     * Obtiene todas las citas
     * GET /api/appointments/
     */
    @GET("appointments/")
    suspend fun getAppointments(): Response<List<AppointmentDto>>

    /**
     * Obtiene una cita específica
     * GET /api/appointments/{id}/
     */
    @GET("appointments/{id}/")
    suspend fun getAppointmentById(@Path("id") appointmentId: String): Response<AppointmentDto>

    /**
     * Crea una nueva cita
     * POST /api/appointments/
     */
    @POST("appointments/")
    suspend fun createAppointment(@Body appointment: AppointmentDto): Response<AppointmentDto>

    /**
     * Actualiza una cita existente
     * PUT /api/appointments/{id}/
     */
    @PUT("appointments/{id}/")
    suspend fun updateAppointment(
        @Path("id") appointmentId: String,
        @Body appointment: AppointmentDto
    ): Response<AppointmentDto>

    /**
     * Cancela una cita (actualización parcial)
     * PATCH /api/appointments/{id}/
     */
    @PATCH("appointments/{id}/")
    suspend fun cancelAppointment(
        @Path("id") appointmentId: String,
        @Body status: Map<String, String>  // {"status": "CANCELLED"}
    ): Response<AppointmentDto>

    /**
     * Elimina una cita
     * DELETE /api/appointments/{id}/
     */
    @DELETE("appointments/{id}/")
    suspend fun deleteAppointment(@Path("id") appointmentId: String): Response<Unit>
}
