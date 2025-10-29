import os
import sys
import django
from datetime import datetime, timedelta

# Configurar encoding para Windows
sys.stdout.reconfigure(encoding='utf-8')

# Configurar Django
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'mediturnapi.settings')
django.setup()

from api.models import Doctor, TimeSlot, Patient, Appointment
from django.utils import timezone

def populate_doctors():
    """Crea doctores de prueba"""
    doctors_data = [
        {
            "name": "Dr. Juan García López",
            "specialty": "CARDIOLOGY",
            "experience": "15 años de experiencia en cardiología",
            "price_per_consultation": 150.00,
            "location": "Lima, Miraflores",
            "about": "Especialista en enfermedades cardiovasculares con amplia experiencia en hospitales de prestigio.",
            "is_telehealth_available": True
        },
        {
            "name": "Dra. María Rodríguez Silva",
            "specialty": "PEDIATRICS",
            "experience": "10 años de experiencia pediátrica",
            "price_per_consultation": 120.00,
            "location": "Lima, San Isidro",
            "about": "Pediatra especializada en desarrollo infantil y vacunación.",
            "is_telehealth_available": True
        },
        {
            "name": "Dr. Carlos López Mendoza",
            "specialty": "DERMATOLOGY",
            "experience": "12 años tratando problemas de piel",
            "price_per_consultation": 130.00,
            "location": "Lima, Surco",
            "about": "Dermatólogo con especialización en tratamientos estéticos y médicos.",
            "is_telehealth_available": False
        },
        {
            "name": "Dra. Ana Martínez Pérez",
            "specialty": "NEUROLOGY",
            "experience": "8 años en neurología clínica",
            "price_per_consultation": 180.00,
            "location": "Lima, San Borja",
            "about": "Neuróloga especializada en trastornos del sistema nervioso.",
            "is_telehealth_available": True
        },
        {
            "name": "Dr. Pedro González Ramírez",
            "specialty": "ORTHOPEDICS",
            "experience": "20 años en traumatología",
            "price_per_consultation": 160.00,
            "location": "Lima, La Molina",
            "about": "Traumatólogo experto en lesiones deportivas y cirugía ortopédica.",
            "is_telehealth_available": False
        },
        {
            "name": "Dra. Laura Fernández Torres",
            "specialty": "GENERAL_MEDICINE",
            "experience": "6 años en medicina general",
            "price_per_consultation": 100.00,
            "location": "Lima, Jesús María",
            "about": "Médico general con enfoque en medicina preventiva y familiar.",
            "is_telehealth_available": True
        }
    ]

    print("Creando doctores...")
    doctors = []
    for data in doctors_data:
        doctor, created = Doctor.objects.get_or_create(
            name=data["name"],
            defaults={
                **data,
                "next_available_slot": timezone.now() + timedelta(days=1)
            }
        )
        doctors.append(doctor)
        status = "✓ Creado" if created else "○ Ya existe"
        print(f"{status}: {doctor.name}")

    return doctors


def populate_timeslots(doctors):
    """Crea time slots para cada doctor"""
    print("\nCreando time slots...")

    for doctor in doctors:
        # Crear slots para los próximos 7 días
        for day in range(1, 8):
            date = timezone.now() + timedelta(days=day)

            # Horarios: 9am, 11am, 2pm, 4pm
            hours = [9, 11, 14, 16]

            for hour in hours:
                slot_datetime = date.replace(hour=hour, minute=0, second=0, microsecond=0)

                timeslot, created = TimeSlot.objects.get_or_create(
                    doctor=doctor,
                    datetime=slot_datetime,
                    defaults={"is_available": True}
                )

                if created:
                    print(f"  ✓ Slot: {doctor.name} - {slot_datetime.strftime('%Y-%m-%d %H:%M')}")


def populate_patients():
    """Crea pacientes de prueba"""
    print("\nCreando pacientes...")

    patients_data = [
        {
            "name": "Carlos Mendoza",
            "email": "carlos.mendoza@email.com",
            "phone": "+51 987654321",
            "date_of_birth": datetime(1990, 5, 15).date()
        },
        {
            "name": "Sofía Torres",
            "email": "sofia.torres@email.com",
            "phone": "+51 912345678",
            "date_of_birth": datetime(1985, 8, 22).date()
        }
    ]

    patients = []
    for data in patients_data:
        patient, created = Patient.objects.get_or_create(
            email=data["email"],
            defaults=data
        )
        patients.append(patient)
        status = "✓ Creado" if created else "○ Ya existe"
        print(f"{status}: {patient.name}")

    return patients


def populate_appointments(doctors, patients):
    """Crea citas de prueba"""
    print("\nCreando citas...")

    if not doctors or not patients:
        print("No hay doctores o pacientes para crear citas")
        return

    appointments_data = [
        {
            "doctor": doctors[0],  # Dr. Juan García (Cardiólogo)
            "patient": patients[0],
            "date": timezone.now() + timedelta(days=2, hours=10),
            "consultation_type": "IN_PERSON",
            "reason": "Control de presión arterial",
            "status": "CONFIRMED"
        },
        {
            "doctor": doctors[1],  # Dra. María Rodríguez (Pediatra)
            "patient": patients[1],
            "date": timezone.now() + timedelta(days=3, hours=15),
            "consultation_type": "TELEHEALTH",
            "reason": "Consulta de vacunación",
            "status": "PENDING"
        }
    ]

    for data in appointments_data:
        appointment, created = Appointment.objects.get_or_create(
            doctor=data["doctor"],
            patient=data["patient"],
            date=data["date"],
            defaults={
                "consultation_type": data["consultation_type"],
                "reason": data["reason"],
                "status": data["status"]
            }
        )
        status = "✓ Creada" if created else "○ Ya existe"
        print(f"{status}: {appointment.doctor.name} - {appointment.patient.name}")


if __name__ == "__main__":
    print("=" * 60)
    print("POBLANDO BASE DE DATOS DE MEDITURN")
    print("=" * 60)

    # Poblar en orden
    doctors = populate_doctors()
    populate_timeslots(doctors)
    patients = populate_patients()
    populate_appointments(doctors, patients)

    print("\n" + "=" * 60)
    print("✓ COMPLETADO")
    print("=" * 60)
    print(f"Doctores: {Doctor.objects.count()}")
    print(f"Time Slots: {TimeSlot.objects.count()}")
    print(f"Pacientes: {Patient.objects.count()}")
    print(f"Citas: {Appointment.objects.count()}")
    print("=" * 60)
