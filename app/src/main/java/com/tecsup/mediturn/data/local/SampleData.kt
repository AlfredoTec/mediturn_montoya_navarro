package com.tecsup.mediturn.data.local

import com.tecsup.mediturn.data.model.*

object SampleData {

//Paciente
    val currentPatient = Patient(
        id = "1",
        name = "Aldy Montoya",
        email = "aldy.montoya@gmail.com",
        phone = "+51 987 654 321",
        dateOfBirth = "15/03/2006"
    )

// Time Slots
    val sampleTimeSlots = listOf(
        TimeSlot("slot_1", "09:00", "Lun 27 Ene", true),
        TimeSlot("slot_2", "10:30", "Lun 27 Ene", true),
        TimeSlot("slot_3", "14:00", "Lun 27 Ene", true),
        TimeSlot("slot_4", "16:30", "Lun 27 Ene", true),
        TimeSlot("slot_5", "09:00", "Mar 28 Ene", true),
        TimeSlot("slot_6", "11:00", "Mar 28 Ene", true),
        TimeSlot("slot_7", "15:00", "Mar 28 Ene", true),
        TimeSlot("slot_8", "09:30", "Mié 29 Ene", true),
        TimeSlot("slot_9", "13:00", "Mié 29 Ene", true),
        TimeSlot("slot_10", "16:00", "Mié 29 Ene", true)
    )

    //Doctores

    val sampleDoctors = listOf(
        Doctor(
            id = "doc_1",
            name = "Dra. María González",
            specialty = "Cardiología",
            rating = 4.9,
            reviewCount = 127,
            experience = "15 años",
            nextAvailableSlot = "Hoy 3:00 PM",
            pricePerConsultation = 120.0,
            isTelehealthAvailable = true,
            location = "Clínica San Pablo, Surco",
            about = "Especialista en cardiología con amplia experiencia en el diagnóstico y tratamiento de enfermedades cardiovasculares. Certificada por la Sociedad Peruana de Cardiología.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_2",
            name = "Dr. Carlos Ramírez",
            specialty = "Medicina General",
            rating = 4.7,
            reviewCount = 89,
            experience = "10 años",
            nextAvailableSlot = "Mañana 10:00 AM",
            pricePerConsultation = 80.0,
            isTelehealthAvailable = true,
            location = "Clínica Ricardo Palma, San Isidro",
            about = "Médico general con enfoque en medicina preventiva y atención primaria. Especializado en el manejo integral de pacientes adultos.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_3",
            name = "Dra. Ana Morales",
            specialty = "Pediatría",
            rating = 4.8,
            reviewCount = 156,
            experience = "12 años",
            nextAvailableSlot = "Hoy 5:00 PM",
            pricePerConsultation = 100.0,
            isTelehealthAvailable = false,
            location = "Hospital Rebagliati, Jesús María",
            about = "Pediatra especializada en el cuidado integral de niños y adolescentes. Experta en vacunación y control de crecimiento y desarrollo.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_4",
            name = "Dr. Luis Torres",
            specialty = "Dermatología",
            rating = 4.6,
            reviewCount = 73,
            experience = "8 años",
            nextAvailableSlot = "Jue 30 Ene 11:00 AM",
            pricePerConsultation = 110.0,
            isTelehealthAvailable = true,
            location = "Dermacentro, Miraflores",
            about = "Dermatólogo especializado en dermatología clínica y estética. Tratamiento de acné, manchas y envejecimiento cutáneo.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_5",
            name = "Dra. Patricia Vega",
            specialty = "Neurología",
            rating = 4.9,
            reviewCount = 98,
            experience = "18 años",
            nextAvailableSlot = "Vie 31 Ene 2:00 PM",
            pricePerConsultation = 150.0,
            isTelehealthAvailable = false,
            location = "Clínica Anglo Americana, San Isidro",
            about = "Neuróloga con subespecialidad en cefaleas y trastornos del movimiento. Amplia experiencia en diagnóstico neurológico.",
            availableTimeSlots = sampleTimeSlots
        ),
        Doctor(
            id = "doc_6",
            name = "Dr. Roberto Flores",
            specialty = "Traumatología",
            rating = 4.7,
            reviewCount = 112,
            experience = "14 años",
            nextAvailableSlot = "Lun 27 Ene 4:00 PM",
            pricePerConsultation = 130.0,
            isTelehealthAvailable = false,
            location = "Clínica San Felipe, Jesús María",
            about = "Traumatólogo y cirujano ortopédico. Especialista en lesiones deportivas y cirugía artroscópica de rodilla y hombro.",
            availableTimeSlots = sampleTimeSlots
        )
    )

// Citas
    val sampleAppointments = listOf(
        Appointment(
            id = "apt_1",
            doctor = sampleDoctors[0], // Dra. María González
            date = "Lun 27 Ene",
            time = "3:00 PM",
            consultationType = ConsultationType.TELEHEALTH,
            reason = "Control de presión arterial",
            status = AppointmentStatus.CONFIRMED
        ),
        Appointment(
            id = "apt_2",
            doctor = sampleDoctors[1], // Dr. Carlos Ramírez
            date = "Mié 29 Ene",
            time = "10:00 AM",
            consultationType = ConsultationType.IN_PERSON,
            reason = "Chequeo general anual",
            status = AppointmentStatus.CONFIRMED
        ),
        Appointment(
            id = "apt_3",
            doctor = sampleDoctors[2], // Dra. Ana Morales
            date = "Vie 31 Ene",
            time = "5:00 PM",
            consultationType = ConsultationType.IN_PERSON,
            reason = "Control pediátrico mensual",
            status = AppointmentStatus.PENDING
        )
    )
}