package com.tecsup.mediturn.ui.screens.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.mediturn.data.model.ConsultationType
import com.tecsup.mediturn.ui.components.TimeSlotButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    doctorId: String,
    onNavigateBack: () -> Unit,
    onConfirmBooking: (String) -> Unit,
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: BookingViewModel = viewModel(factory = viewModelFactory)
) {
    LaunchedEffect(doctorId) {
        viewModel.loadDoctor(doctorId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Agendar Cita",
                        color = colorScheme.onPrimary,
                        style = typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = colorScheme.surface
            ) {
                Button(
                    onClick = {
                        val appointmentId = viewModel.bookAppointment()
                        if (appointmentId.isNotEmpty()) onConfirmBooking(appointmentId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = uiState.selectedTimeSlot != null && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            "Confirmar Reserva",
                            modifier = Modifier.padding(8.dp),
                            style = typography.labelLarge
                        )
                    }
                }
            }
        },
        containerColor = colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Doctor Info
            item {
                uiState.doctor?.let { doctor ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorScheme.surface
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = doctor.name,
                                style = typography.titleMedium,
                                color = colorScheme.onSurface
                            )
                            Text(
                                text = doctor.specialty.displayName,
                                color = colorScheme.onSurfaceVariant,
                                style = typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Consultation Type
            item {
                Column {
                    Text(
                        text = "Tipo de Consulta",
                        style = typography.titleMedium,
                        color = colorScheme.onBackground
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ConsultationTypeCard(
                            title = "Presencial",
                            icon = Icons.Default.LocalHospital,
                            isSelected = uiState.consultationType == ConsultationType.IN_PERSON,
                            onClick = {
                                viewModel.onConsultationTypeChanged(ConsultationType.IN_PERSON)
                            },
                            modifier = Modifier.weight(1f)
                        )

                        if (uiState.doctor?.isTelehealthAvailable == true) {
                            ConsultationTypeCard(
                                title = "Teleconsulta",
                                icon = Icons.Default.VideoCall,
                                isSelected = uiState.consultationType == ConsultationType.TELEHEALTH,
                                onClick = {
                                    viewModel.onConsultationTypeChanged(ConsultationType.TELEHEALTH)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Time Slots
            item {
                Column {
                    Text(
                        text = "Selecciona Fecha y Hora",
                        style = typography.titleMedium,
                        color = colorScheme.onBackground
                    )
                    Spacer(Modifier.height(12.dp))

                    uiState.doctor?.availableTimeSlots?.let { slots ->
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(slots) { slot ->
                                TimeSlotButton(
                                    date = slot.dateTime,
                                    isSelected = uiState.selectedTimeSlot == slot,
                                    isAvailable = slot.isAvailable,
                                    onClick = { viewModel.onTimeSlotSelected(slot) }
                                )
                            }
                        }
                    }

                    uiState.timeSlotError?.let { error ->
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = error,
                            color = colorScheme.error,
                            style = typography.bodySmall
                        )
                    }
                }
            }

            // Reason
            item {
                Column {
                    Text(
                        text = "Motivo de la Consulta (Opcional)",
                        style = typography.titleMedium,
                        color = colorScheme.onBackground
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.reason,
                        onValueChange = { viewModel.onReasonChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Describe tu motivo...") },
                        minLines = 3,
                        maxLines = 6,
                        isError = uiState.reasonError != null,
                        supportingText = {
                            uiState.reasonError?.let { error ->
                                Text(text = error, color = colorScheme.error)
                            }
                            if (uiState.reasonError == null) {
                                Text(
                                    text = "${uiState.reason.length}/500",
                                    color = colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            cursorColor = colorScheme.primary,
                            focusedTextColor = colorScheme.onBackground
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConsultationTypeCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colorScheme.primary else colorScheme.surface
        ),
        border = if (!isSelected) BorderStroke(1.dp, colorScheme.outlineVariant) else null,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (isSelected) colorScheme.onPrimary else colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
            )
        }
    }
}
