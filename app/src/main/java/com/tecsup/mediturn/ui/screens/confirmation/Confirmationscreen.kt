package com.tecsup.mediturn.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    appointmentId: String,
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmación") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Confirmation Screen",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Appointment ID: $appointmentId",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Implementar en Día 3",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateToHome) {
                Text("Volver al Inicio")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onNavigateToAppointments) {
                Text("Ver Mis Citas")
            }
        }
    }
}