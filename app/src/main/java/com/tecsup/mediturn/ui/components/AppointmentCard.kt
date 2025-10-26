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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import com.tecsup.mediturn.data.model.ConsultationType
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onCancelClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale("es", "ES"))

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 🟢 Encabezado con estado y tipo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indicador de estado
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            when (appointment.status) {
                                AppointmentStatus.CONFIRMED -> Color(0xFFB4EC51)
                                AppointmentStatus.PENDING -> Color(0xFFFFC107)
                                AppointmentStatus.CANCELLED -> Color(0xFFEF4444)
                                AppointmentStatus.COMPLETED -> Color(0xFF22C55E)
                            }
                        )
                )

                // Tipo de consulta
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon = if (appointment.consultationType == ConsultationType.TELEHEALTH)
                        Icons.Default.VideoCall else Icons.Default.LocalHospital
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (appointment.consultationType == ConsultationType.TELEHEALTH)
                            "Teleconsulta" else "Presencial",
                        style = typography.labelSmall.copy(
                            color = colors.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            // 👩‍⚕️ Información del doctor
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(colors.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = appointment.doctor.name.split(" ")
                            .map { it.first() }
                            .take(2)
                            .joinToString(""),
                        style = typography.titleMedium.copy(color = colors.onPrimaryContainer)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = appointment.doctor.name,
                        style = typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onSurface
                        )
                    )
                    Text(
                        text = appointment.doctor.specialty.displayName,
                        style = typography.bodyMedium.copy(color = colors.onSurfaceVariant)
                    )
                }
            }

            Divider(color = colors.outlineVariant)

            // 🕒 Fecha y hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Fecha",
                        style = typography.labelSmall.copy(color = colors.onSurfaceVariant)
                    )
                    Text(
                        text = dateFormatter.format(appointment.date),
                        style = typography.bodyMedium.copy(color = colors.onSurface)
                    )
                }
                Column {
                    Text(
                        text = "Hora",
                        style = typography.labelSmall.copy(color = colors.onSurfaceVariant)
                    )
                    Text(
                        text = timeFormatter.format(appointment.date),
                        style = typography.bodyMedium.copy(color = colors.onSurface)
                    )
                }
            }

            // 📝 Motivo
            if (appointment.reason.isNotBlank()) {
                Column {
                    Text(
                        text = "Motivo",
                        style = typography.labelSmall.copy(color = colors.onSurfaceVariant)
                    )
                    Text(
                        text = appointment.reason,
                        style = typography.bodyMedium.copy(color = colors.onSurface)
                    )
                }
            }

            // ❌ Botón cancelar
            if (onCancelClick != null && appointment.status == AppointmentStatus.CONFIRMED) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.error
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = SolidColor(colors.error)
                    )
                ) {
                    Text("Cancelar cita")
                }
            }
        }
    }
}
