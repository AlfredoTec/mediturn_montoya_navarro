from django.db import models
from django.utils import timezone
import uuid


# --- ENUMS (similares a los Enum de Kotlin) ---
class ConsultationType(models.TextChoices):
    IN_PERSON = "IN_PERSON", "Presencial"
    TELEHEALTH = "TELEHEALTH", "Teleconsulta"


class AppointmentStatus(models.TextChoices):
    CONFIRMED = "CONFIRMED", "Confirmada"
    PENDING = "PENDING", "Pendiente"
    CANCELLED = "CANCELLED", "Cancelada"
    COMPLETED = "COMPLETED", "Completada"


class Specialty(models.TextChoices):
    GENERAL_MEDICINE = "GENERAL_MEDICINE", "Medicina General"
    CARDIOLOGY = "CARDIOLOGY", "Cardiología"
    PEDIATRICS = "PEDIATRICS", "Pediatría"
    DERMATOLOGY = "DERMATOLOGY", "Dermatología"
    NEUROLOGY = "NEUROLOGY", "Neurología"
    ORTHOPEDICS = "ORTHOPEDICS", "Traumatología"


# --- MODELOS BASE ---
class Doctor(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField(max_length=100)
    specialty = models.CharField(max_length=50, choices=Specialty.choices)
    experience = models.CharField(max_length=100)
    next_available_slot = models.DateTimeField(default=timezone.now)
    price_per_consultation = models.DecimalField(max_digits=8, decimal_places=2)
    image_url = models.URLField(blank=True, null=True)  # En lugar de imageResId
    is_telehealth_available = models.BooleanField(default=False)
    location = models.CharField(max_length=150)
    about = models.TextField(blank=True, default="")

    def __str__(self):
        return f"{self.name} - {self.get_specialty_display()}"


class TimeSlot(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE, related_name="time_slots")
    datetime = models.DateTimeField()
    is_available = models.BooleanField(default=True)

    def __str__(self):
        return f"{self.doctor.name} - {self.datetime}"


class Patient(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField(max_length=100)
    email = models.EmailField(unique=True)
    phone = models.CharField(max_length=20, blank=True, default="")
    date_of_birth = models.DateField()

    def __str__(self):
        return self.name


class Appointment(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE, related_name="appointments")
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE, related_name="appointments")
    date = models.DateTimeField(default=timezone.now)
    consultation_type = models.CharField(max_length=20, choices=ConsultationType.choices)
    reason = models.TextField(blank=True, default="")
    status = models.CharField(max_length=20, choices=AppointmentStatus.choices, default=AppointmentStatus.CONFIRMED)

    def __str__(self):
        return f"Cita con {self.doctor.name} ({self.get_status_display()})"
