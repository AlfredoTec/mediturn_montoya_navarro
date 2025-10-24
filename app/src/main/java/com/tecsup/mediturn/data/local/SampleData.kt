package com.tecsup.mediturn.data.local

import com.tecsup.mediturn.data.model.*

object SampleData {

    val sampleTimeSlots = listOf(
        TimeSlot("1", "3:00 PM", "2025-10-25", true),
        TimeSlot("2", "4:00 PM", "2025-10-25", true),
        TimeSlot("3", "5:00 PM", "2025-10-25", true),
        TimeSlot("4", "6:00 PM", "2025-10-25", false),
        TimeSlot("5", "7:00 PM", "2025-10-25", true)
    )

    val sampleDoctors = listOf(
        Doctor(
            id = "1",
            name = "Dra. María González",
            specialty = "Cardiología",
            rating = 4.9,
            reviewCount = 127,
            experience = "15 años",
            nextAvailableSlot = "Hoy 3:00 PM",
            pricePerConsultation = 45.0,
            imageUrl = "",
            isTelehealthAvailable = true,
            location = "Hospital Central, Lima",
            about = "Especialista en Cardiología con más de 15 años de experiencia.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "2",
            name = "Dr. Carlos Ruiz",
            specialty = "Medicina General",
            rating = 4.8,
            reviewCount = 89,
            experience = "10 años",
            nextAvailableSlot = "Mañana 10:00 AM",
            pricePerConsultation = 35.0,
            imageUrl = "",
            isTelehealthAvailable = true,
            location = "Clínica San Pablo, Lima",
            about = "Médico general con amplia experiencia.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "3",
            name = "Dra. Ana Martínez",
            specialty = "Pediatría",
            rating = 4.9,
            reviewCount = 156,
            experience = "12 años",
            nextAvailableSlot = "Hoy 4:00 PM",
            pricePerConsultation = 40.0,
            imageUrl = "",
            isTelehealthAvailable = false,
            location = "Clínica Internacional, Lima",
            about = "Pediatra especializada en el cuidado de niños.",
            availableTimeSlots = sampleTimeSlots
        )
    )

    val sampleAppointments = listOf(
        Appointment(
            id = "1",
            doctor = sampleDoctors[0],
            date = "25 Oct 2025",
            time = "3:00 PM",
            consultationType = ConsultationType.IN_PERSON,
            reason = "Control rutinario",
            status = AppointmentStatus.CONFIRMED
        ),
        Appointment(
            id = "2",
            doctor = sampleDoctors[1],
            date = "28 Oct 2025",
            time = "10:00 AM",
            consultationType = ConsultationType.TELEHEALTH,
            reason = "Revisión de resultados",
            status = AppointmentStatus.PENDING
        )
    )
}