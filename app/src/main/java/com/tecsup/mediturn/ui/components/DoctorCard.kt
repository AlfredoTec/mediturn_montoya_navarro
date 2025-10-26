package com.tecsup.mediturn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.mediturn.data.model.Doctor
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DoctorCard(
    doctor: Doctor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 🔹 Formateador de fecha y hora
    val dateFormatter = SimpleDateFormat("EEE dd MMM, hh:mm a", Locale("es", "PE"))
    val formattedDate = dateFormatter.format(doctor.nextAvailableSlot)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 🩺 Avatar circular del doctor
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2563EB)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = doctor.name.split(" ").map { it.first() }.take(2).joinToString(""),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // 🧠 Información principal del doctor
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Nombre del doctor
                Text(
                    text = doctor.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )

                // Especialidad
                Text(
                    text = doctor.specialty.displayName,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )

                // 📅 Próxima disponibilidad (formateada)
                Text(
                    text = "🕐 Próx. disponible: $formattedDate",
                    fontSize = 12.sp,
                    color = Color(0xFF10B981)
                )

                // 💰 Precio y teleconsulta
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "S/ ${doctor.pricePerConsultation}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB)
                    )

                    if (doctor.isTelehealthAvailable) {
                        Icon(
                            imageVector = Icons.Default.VideoCall,
                            contentDescription = "Teleconsulta disponible",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
