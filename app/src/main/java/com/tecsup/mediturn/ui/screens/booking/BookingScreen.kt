package com.tecsup.mediturn.ui.screens.booking

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.mediturn.data.model.ConsultationType
import com.tecsup.mediturn.ui.components.TimeSlotButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    doctorId: String,
    onNavigateBack: () -> Unit,
    onConfirmBooking: (String) -> Unit,
    viewModel: BookingViewModel = viewModel()
) {
    LaunchedEffect(doctorId) {
        viewModel.loadDoctor(doctorId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Cita") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = {
                        val appointmentId = viewModel.bookAppointment()
                        if (appointmentId.isNotEmpty()) {
                            onConfirmBooking(appointmentId)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = uiState.selectedTimeSlot != null
                ) {
                    Text("Confirmar Reserva", modifier = Modifier.padding(8.dp))
                }
            }
        }
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
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = doctor.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = doctor.specialty,
                                color = Color(0xFF6B7280)
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(12.dp))

                    uiState.doctor?.availableTimeSlots?.let { slots ->
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(slots) { slot ->
                                TimeSlotButton(
                                    time = slot.time,
                                    date = slot.date,
                                    isSelected = uiState.selectedTimeSlot == slot,
                                    isAvailable = slot.isAvailable,
                                    onClick = { viewModel.onTimeSlotSelected(slot) }
                                )
                            }
                        }
                    }
                }
            }

            // Reason
            item {
                Column {
                    Text(
                        text = "Motivo de la Consulta (Opcional)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.reason,
                        onValueChange = { viewModel.onReasonChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Describe tu motivo...") },
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp)
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
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2563EB) else Color.White
        ),
        border = if (!isSelected) {
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
        } else null,
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
                tint = if (isSelected) Color.White else Color(0xFF2563EB)
            )
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF1F2937)
            )
        }
    }
}