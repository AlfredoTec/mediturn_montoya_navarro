package com.tecsup.mediturn.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailScreen(
    doctorId: String,
    viewModel: DoctorDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToBooking: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(doctorId) {
        viewModel.loadDoctor(doctorId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Médico") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
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
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.doctor != null -> {
                    val doctor = uiState.doctor!!
                    Text(doctor.name, style = MaterialTheme.typography.headlineLarge)
                    Text(doctor.specialty, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Rating: ${doctor.rating} ⭐")
                    Text("Experiencia: ${doctor.experience}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onNavigateToBooking(doctorId) }) {
                        Text("Agendar Cita")
                    }
                }
                else -> Text("Doctor no encontrado")
            }
        }
    }
}