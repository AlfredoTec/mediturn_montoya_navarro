package com.tecsup.mediturn.data.local

import com.tecsup.mediturn.R
import com.tecsup.mediturn.data.model.*
import java.text.SimpleDateFormat
import java.util.*

object SampleData {

    // Formatos de fecha y hora
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    // Time Slots
    val sampleTimeSlots = listOf(
        TimeSlot("slot_1", dateFormat.parse("27/01/2025 09:00")!!),
        TimeSlot("slot_2", dateFormat.parse("27/01/2025 10:30")!!),
        TimeSlot("slot_3", dateFormat.parse("27/01/2025 14:00")!!),
        TimeSlot("slot_4", dateFormat.parse("27/01/2025 16:30")!!),
        TimeSlot("slot_5", dateFormat.parse("28/01/2025 09:00")!!),
        TimeSlot("slot_6", dateFormat.parse("28/01/2025 11:00")!!),
        TimeSlot("slot_7", dateFormat.parse("28/01/2025 15:00")!!),
        TimeSlot("slot_8", dateFormat.parse("29/01/2025 09:30")!!),
        TimeSlot("slot_9", dateFormat.parse("29/01/2025 13:00")!!),
        TimeSlot("slot_10", dateFormat.parse("29/01/2025 16:00")!!)
    )

    // Doctores (specialty -> enum) con imágenes en drawable-nodpi
    val sampleDoctors = listOf(
        Doctor(
            id = "doc_1",
            name = "Dra. María González",
            specialty = Specialty.CARDIOLOGY,
            experience = "15 años",
            nextAvailableSlot = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000), // Hoy +2 horas
            pricePerConsultation = 120.0,
            imageResId = R.drawable.doctor_1,
            isTelehealthAvailable = true,
            location = "Clínica San Pablo, Surco",
            about = "Especialista en cardiología con amplia experiencia en el diagnóstico y tratamiento de enfermedades cardiovasculares.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_2",
            name = "Dr. Carlos Ramírez",
            specialty = Specialty.GENERAL_MEDICINE,
            experience = "10 años",
            nextAvailableSlot = Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 10 * 60 * 60 * 1000), // Mañana 10 AM aprox
            pricePerConsultation = 80.0,
            imageResId = R.drawable.doctor_2,
            isTelehealthAvailable = true,
            location = "Clínica Ricardo Palma, San Isidro",
            about = "Médico general con enfoque en medicina preventiva y atención primaria.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_3",
            name = "Dra. Ana Morales",
            specialty = Specialty.PEDIATRICS,
            experience = "12 años",
            nextAvailableSlot = Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000), // Hoy +5 horas
            pricePerConsultation = 100.0,
            imageResId = R.drawable.doctor_3,
            isTelehealthAvailable = false,
            location = "Hospital Rebagliati, Jesús María",
            about = "Pediatra especializada en el cuidado integral de niños y adolescentes.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_4",
            name = "Dr. Luis Torres",
            specialty = Specialty.DERMATOLOGY,
            experience = "8 años",
            nextAvailableSlot = Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000 + 11 * 60 * 60 * 1000), // Jueves 11 AM aprox
            pricePerConsultation = 110.0,
            imageResId = R.drawable.doctor_4,
            isTelehealthAvailable = true,
            location = "Dermacentro, Miraflores",
            about = "Dermatólogo especializado en dermatología clínica y estética.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_5",
            name = "Dra. Patricia Vega",
            specialty = Specialty.NEUROLOGY,
            experience = "18 años",
            nextAvailableSlot = Date(System.currentTimeMillis() + 6 * 24 * 60 * 60 * 1000 + 14 * 60 * 60 * 1000), // Viernes 2 PM aprox
            pricePerConsultation = 150.0,
            imageResId = R.drawable.doctor_5,
            isTelehealthAvailable = false,
            location = "Clínica Anglo Americana, San Isidro",
            about = "Neuróloga con subespecialidad en cefaleas y trastornos del movimiento.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_6",
            name = "Dr. Roberto Flores",
            specialty = Specialty.ORTHOPEDICS,
            experience = "14 años",
            nextAvailableSlot = Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000 + 16 * 60 * 60 * 1000), // Lunes 4 PM aprox
            pricePerConsultation = 130.0,
            imageResId = R.drawable.doctor_6,
            isTelehealthAvailable = false,
            location = "Clínica San Felipe, Jesús María",
            about = "Traumatólogo especializado en lesiones deportivas y cirugía artroscópica.",
            availableTimeSlots = sampleTimeSlots
        )
    )

    // Citas (Appointment con Date)
    val sampleAppointments = listOf(
        Appointment(
            id = "apt_1",
            doctor = sampleDoctors[0],
            date = dateFormat.parse("27/10/2025 15:00")!!,  // Lun 27 Oct 3:00 PM
            consultationType = ConsultationType.TELEHEALTH,
            reason = "Control de presión arterial",
            status = AppointmentStatus.CONFIRMED
        ),
        Appointment(
            id = "apt_2",
            doctor = sampleDoctors[1],
            date = dateFormat.parse("29/10/2025 10:00")!!,  // Mié 29 Oct 10:00 AM
            consultationType = ConsultationType.IN_PERSON,
            reason = "Chequeo general anual",
            status = AppointmentStatus.CONFIRMED
        ),
        Appointment(
            id = "apt_3",
            doctor = sampleDoctors[2],
            date = dateFormat.parse("25/10/2025 20:00")!!,  // Sab 25 Oct 8:00 PM
            consultationType = ConsultationType.IN_PERSON,
            reason = "Control pediátrico mensual",
            status = AppointmentStatus.PENDING
        ),
        Appointment(
            id = "apt_4",
            doctor = sampleDoctors[0],
            date = dateFormat.parse("20/09/2025 12:00")!!,  // Sab 20 Sep 12:00 PM
            consultationType = ConsultationType.TELEHEALTH,
            reason = "Control de presión arterial",
            status = AppointmentStatus.COMPLETED
        )
    )
}
