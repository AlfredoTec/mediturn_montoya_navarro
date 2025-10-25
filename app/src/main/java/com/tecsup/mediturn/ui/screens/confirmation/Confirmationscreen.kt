package com.tecsup.mediturn.ui.screens.confirmation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.mediturn.ui.components.AppointmentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    appointmentId: String,
    onGoToHome: () -> Unit,
    onViewAppointments: () -> Unit,
    viewModel: ConfirmationViewModel = viewModel()
) {
    LaunchedEffect(appointmentId) {
        viewModel.loadAppointment(appointmentId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmación") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(32.dp))

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color(0xFF10B981)
            )

            Text(
                text = "¡Cita Agendada!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Text(
                text = "Tu cita ha sido confirmada exitosamente.\nRecibirás un recordatorio antes de la consulta.",
                fontSize = 16.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                uiState.appointment?.let { appointment ->
                    AppointmentCard(appointment = appointment)
                }
            }

            Spacer(Modifier.weight(1f))

            // Buttons
            Button(
                onClick = onViewAppointments,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver Mis Citas", modifier = Modifier.padding(8.dp))
            }

            OutlinedButton(
                onClick = onGoToHome,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver al Inicio", modifier = Modifier.padding(8.dp))
            }
        }
    }
}