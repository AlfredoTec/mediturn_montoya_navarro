package com.tecsup.mediturn.ui.screens.appointments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.mediturn.MediTurnApplication
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.AppointmentStatus
import com.tecsup.mediturn.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    patientId: String,
    onAppointmentClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val app = context.applicationContext as MediTurnApplication

    val viewModel: AppointmentsViewModel = viewModel(
        factory = AppointmentsViewModelFactory(app.repository, patientId)
    )

    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar mensaje de éxito
    LaunchedEffect(state.operationSuccess, state.operationMessage) {
        if (state.operationSuccess && state.operationMessage != null) {
            snackbarHostState.showSnackbar(state.operationMessage!!)
            viewModel.resetOperation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Citas") },
                actions = {
                    IconButton(onClick = { viewModel.loadAppointments() }) {
                        Icon(Icons.Default.Refresh, "Actualizar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Próximas")
                            if (state.upcomingAppointments.isNotEmpty()) {
                                Badge { Text(state.upcomingAppointments.size.toString()) }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.CalendarToday, null) }
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Pasadas") },
                    icon = { Icon(Icons.Default.History, null) }
                )

                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Todas") },
                    icon = { Icon(Icons.Default.ListAlt, null) }
                )
            }

            // Contenido según el tab seleccionado
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        val appointments = when (selectedTab) {
                            0 -> state.upcomingAppointments
                            1 -> state.pastAppointments
                            else -> state.allAppointments
                        }

                        if (appointments.isEmpty()) {
                            EmptyAppointmentsMessage(
                                isUpcoming = selectedTab == 0,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(appointments) { appointment ->
                                    AppointmentCard(
                                        appointment = appointment,
                                        onCancel = {
                                            viewModel.showCancelDialog(appointment)
                                        },
                                        onReschedule = {
                                            viewModel.showRescheduleDialog(appointment)
                                        },
                                        onClick = { onAppointmentClick(appointment.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de cancelación
        if (state.showCancelDialog && state.selectedAppointment != null) {
            CancelAppointmentDialog(
                appointment = state.selectedAppointment!!,
                onConfirm = {
                    viewModel.cancelAppointment(state.selectedAppointment!!.id)
                },
                onDismiss = { viewModel.hideCancelDialog() }
            )
        }

        // Diálogo de reprogramación
        if (state.showRescheduleDialog && state.selectedAppointment != null) {
            RescheduleDialog(
                appointment = state.selectedAppointment!!,
                availableSlots = state.availableRescheduleSlots,
                onConfirm = { slotId, newDate ->
                    viewModel.rescheduleAppointment(
                        state.selectedAppointment!!.id,
                        slotId,
                        newDate
                    )
                },
                onDismiss = { viewModel.hideRescheduleDialog() }
            )
        }
    }
}

@Composable
fun EmptyAppointmentsMessage(
    isUpcoming: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = if (isUpcoming) Icons.Default.EventBusy else Icons.Default.History,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = if (isUpcoming) "No tienes citas próximas" else "No tienes citas pasadas",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (isUpcoming) {
            Text(
                text = "Agenda una cita con tu médico",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCard(
    appointment: Appointment,
    onCancel: () -> Unit,
    onReschedule: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPast = DateUtils.isPast(appointment.date)
    val isCancelled = appointment.status == AppointmentStatus.CANCELLED

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isCancelled -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header con doctor y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = appointment.doctor.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                AppointmentStatusChip(status = appointment.status)
            }

            // Especialidad
            Text(
                text = appointment.doctor.specialty.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(appointment.doctor.specialty.color)
            )

            // Fecha y hora
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = DateUtils.getRelativeTimeString(appointment.date),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Tipo de consulta
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (appointment.consultationType == com.tecsup.mediturn.data.model.ConsultationType.TELEHEALTH)
                        Icons.Default.Videocam else Icons.Default.LocalHospital,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (appointment.consultationType == com.tecsup.mediturn.data.model.ConsultationType.TELEHEALTH)
                        "Telesalud" else "Presencial",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Ubicación
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = appointment.doctor.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Motivo
            if (appointment.reason.isNotEmpty()) {
                Text(
                    text = "Motivo: ${appointment.reason}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            // Acciones (solo para citas futuras y confirmadas)
            if (!isPast && !isCancelled) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onReschedule,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Reprogramar")
                    }

                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentStatusChip(status: AppointmentStatus) {
    val (text, color) = when (status) {
        AppointmentStatus.CONFIRMED -> "Confirmada" to MaterialTheme.colorScheme.primary
        AppointmentStatus.PENDING -> "Pendiente" to MaterialTheme.colorScheme.tertiary
        AppointmentStatus.CANCELLED -> "Cancelada" to MaterialTheme.colorScheme.error
        AppointmentStatus.COMPLETED -> "Completada" to MaterialTheme.colorScheme.secondary
    }

    AssistChip(
        onClick = { },
        label = { Text(text) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color.copy(alpha = 0.2f),
            labelColor = color
        )
    )
}

@Composable
fun CancelAppointmentDialog(
    appointment: Appointment,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = { Text("¿Cancelar cita?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Estás a punto de cancelar tu cita con:")
                Text(
                    text = appointment.doctor.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = DateUtils.formatAppointmentDate(appointment.date),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Esta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cancelar Cita")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Volver")
            }
        }
    )
}

@Composable
fun RescheduleDialog(
    appointment: Appointment,
    availableSlots: List<com.tecsup.mediturn.data.model.TimeSlot>,
    onConfirm: (String, java.util.Date) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedSlot by remember { mutableStateOf<com.tecsup.mediturn.data.model.TimeSlot?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reprogramar Cita") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text("Selecciona un nuevo horario:")
                }

                if (availableSlots.isEmpty()) {
                    item {
                        Text(
                            text = "No hay horarios disponibles",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    val slotsByDay = availableSlots.groupBy {
                        DateUtils.formatDate(it.dateTime)
                    }

                    slotsByDay.forEach { (date, slots) ->
                        item {
                            Text(
                                text = date,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        items(slots) { slot ->
                            FilterChip(
                                selected = selectedSlot?.id == slot.id,
                                onClick = { selectedSlot = slot },
                                label = { Text(DateUtils.formatTime(slot.dateTime)) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedSlot?.let {
                        onConfirm(it.id, it.dateTime)
                    }
                },
                enabled = selectedSlot != null
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}