package com.tecsup.mediturn.ui.screens.confirmation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Confirmación",
                        style = typography.titleLarge,
                        color = colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary
                )
            )
        },
        containerColor = colorScheme.background
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
                tint = colorScheme.tertiary // verde en light, variante adecuada en dark
            )

            Text(
                text = "¡Cita Agendada!",
                style = typography.headlineSmall,
                color = colorScheme.onBackground
            )

            Text(
                text = "Tu cita ha sido confirmada exitosamente.\nRecibirás un recordatorio antes de la consulta.",
                style = typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(color = colorScheme.primary)
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
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                )
            ) {
                Text("Ver Mis Citas", modifier = Modifier.padding(8.dp))
            }

            OutlinedButton(
                onClick = onGoToHome,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorScheme.primary
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(colorScheme.primary)
                )
            ) {
                Text("Volver al Inicio", modifier = Modifier.padding(8.dp))
            }
        }
    }
}
