package com.tecsup.mediturn.ui.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.VideoCall
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
    onNavigateToConfirmation: (String) -> Unit,
    viewModel: BookingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(doctorId) {
        viewModel.loadDoctor(doctorId)
    }

    // Navigate to confirmation when booking is complete
    LaunchedEffect(uiState.bookingComplete) {
        if (uiState.bookingComplete && uiState.appointmentId != null) {
            onNavigateToConfirmation(uiState.appointmentId!!)
        }
    }

    // Show error snackbar
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Show snackbar (would need SnackbarHost in real implementation)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Cita") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Doctor Info
                item {
                    uiState.doctor?.let { doctor ->
                        DoctorInfoSection(doctor = doctor)
                    }
                }

                // Consultation Type
                item {
                    ConsultationTypeSection(
                        selectedType = uiState.selectedConsultationType,
                        isTelehealthAvailable = uiState.doctor?.isTelehealthAvailable ?: false,
                        onTypeSelected = { viewModel.selectConsultationType(it) }
                    )
                }

                // Date Selection
                item {
                    DateSelectionSection(
                        availableDates = uiState.availableDates,
                        selectedDate = uiState.selectedDate,
                        onDateSelected = { viewModel.selectDate(it) }
                    )
                }

                // Time Selection
                item {
                    TimeSelectionSection(
                        availableSlots = uiState.availableTimeSlots.map { it.time },
                        selectedTime = uiState.selectedTime,
                        onTimeSelected = { viewModel.selectTime(it) }
                    )
                }

                // Reason
                item {
                    ReasonSection(
                        reason = uiState.reason,
                        onReasonChange = { viewModel.updateReason(it) }
                    )
                }

                // Book Button
                item {
                    Button(
                        onClick = { viewModel.bookAppointment() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB)
                        )
                    ) {
                        Text(
                            text = "Confirmar Cita",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DoctorInfoSection(doctor: com.tecsup.mediturn.data.model.Doctor) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = doctor.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = doctor.specialty,
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
            Text(
                text = doctor.location,
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
private fun ConsultationTypeSection(
    selectedType: ConsultationType,
    isTelehealthAvailable: Boolean,
    onTypeSelected: (ConsultationType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Tipo de Consulta",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ConsultationTypeCard(
                title = "Presencial",
                icon = Icons.Default.LocationOn,
                isSelected = selectedType == ConsultationType.IN_PERSON,
                onClick = { onTypeSelected(ConsultationType.IN_PERSON) },
                modifier = Modifier.weight(1f)
            )

            ConsultationTypeCard(
                title = "Teleconsulta",
                icon = Icons.Default.VideoCall,
                isSelected = selectedType == ConsultationType.TELEHEALTH,
                enabled = isTelehealthAvailable,
                onClick = { onTypeSelected(ConsultationType.TELEHEALTH) },
                modifier = Modifier.weight(1f)
            )
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
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2563EB).copy(alpha = 0.1f)
            else Color.White,
            disabledContainerColor = Color(0xFFF3F4F6)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            width = if (isSelected) 2.dp else 1.dp,
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isSelected) Color(0xFF2563EB) else Color(0xFFE5E7EB)
            )
        )
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
                tint = if (isSelected) Color(0xFF2563EB) else Color(0xFF6B7280),
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                color = if (enabled) Color(0xFF1F2937) else Color(0xFF9CA3AF)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelectionSection(
    availableDates: List<String>,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Fecha",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableDates) { date ->
                FilterChip(
                    selected = selectedDate == date,
                    onClick = { onDateSelected(date) },
                    label = { Text(date) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2563EB),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
private fun TimeSelectionSection(
    availableSlots: List<String>,
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Horario",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            availableSlots.chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { time ->
                        TimeSlotButton(
                            time = time,
                            isSelected = selectedTime == time,
                            onClick = { onTimeSelected(time) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill remaining space if row is not complete
                    repeat(3 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ReasonSection(
    reason: String,
    onReasonChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Motivo de la Consulta",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )

        OutlinedTextField(
            value = reason,
            onValueChange = onReasonChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = {
                Text("Describe brevemente el motivo de tu consulta...")
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2563EB),
                unfocusedBorderColor = Color(0xFFE5E7EB)
            )
        )
    }
}