package com.tecsup.mediturn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import com.tecsup.mediturn.data.model.ConsultationType

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onCancelClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // encabezado con estados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // indicador de estado
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            when (appointment.status) {
                                AppointmentStatus.CONFIRMED -> Color(0xFF10B981)
                                AppointmentStatus.PENDING -> Color(0xFFF59E0B)
                                AppointmentStatus.CANCELLED -> Color(0xFFEF4444)
                                AppointmentStatus.COMPLETED -> Color(0xFF6B7280)
                            }
                        )
                )

                // tipo de consulta
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (appointment.consultationType == ConsultationType.TELEHEALTH)
                            Icons.Default.VideoCall else Icons.Default.LocalHospital,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (appointment.consultationType == ConsultationType.TELEHEALTH)
                            "Teleconsulta" else "Presencial",
                        fontSize = 12.sp,
                        color = Color(0xFF2563EB)
                    )
                }
            }

            // informacion de doctor
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2563EB)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = appointment.doctor.name.split(" ").map { it.first() }.take(2).joinToString(""),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = appointment.doctor.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = appointment.doctor.specialty,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Divider(color = Color(0xFFE5E7EB))

            // cita y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Fecha",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = appointment.date,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1F2937)
                    )
                }

                Column {
                    Text(
                        text = "Hora",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = appointment.time,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1F2937)
                    )
                }
            }

            // razon si es viable
            if (appointment.reason.isNotBlank()) {
                Column {
                    Text(
                        text = "Motivo",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = appointment.reason,
                        fontSize = 14.sp,
                        color = Color(0xFF1F2937)
                    )
                }
            }

            // boton para cancelar cita
            if (onCancelClick != null && appointment.status == AppointmentStatus.CONFIRMED) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFEF4444)
                    )
                ) {
                    Text("Cancelar cita")
                }
            }
        }
    }
}