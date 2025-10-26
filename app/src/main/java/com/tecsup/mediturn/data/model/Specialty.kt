package com.tecsup.mediturn.data.model

// Clase que representa las especialidades médicas
enum class Specialty(val displayName: String, val color: Long) {
    GENERAL_MEDICINE("Medicina General", 0xFF4ECDC4),
    CARDIOLOGY("Cardiología", 0xFFFF6B9D),
    PEDIATRICS("Pediatría", 0xFF95E1D3),
    DERMATOLOGY("Dermatología", 0xFFFFA07A),
    NEUROLOGY("Neurología", 0xFF9370DB),
    ORTHOPEDICS("Traumatología", 0xFF87CEEB);

    companion object {

        // Obtiene todas las especialidades
        fun getAll(): List<Specialty> {
            return Specialty.entries
        }

         // Obtiene los nombres para mostrar en UI
        fun getDisplayNames(): List<String> {
            return Specialty.entries.map { it.displayName }
        }
    }
}