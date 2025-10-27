package com.tecsup.mediturn.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.mediturn.data.model.SampleData
import com.tecsup.mediturn.ui.components.DoctorCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onDoctorClick: (String) -> Unit,
    onAppointmentsClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
) {
    val patient = SampleData.currentPatient
    val uiState by viewModel.uiState.collectAsState()

    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Bienvenido",
                            style = typography.bodySmall,
                            color = colorScheme.onSurfaceVariant
                        )
                        Text(
                            patient.name,
                            style = typography.titleMedium,
                            color = colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    // Badge de notificaciones
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = colorScheme.error,
                                contentColor = colorScheme.onError
                            ) {
                                Text("3")
                            }
                        }
                    ) {
                        IconButton(onClick = { /* TODO: Abrir notificaciones */ }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.surface
                )
            )
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
            // Barra de búsqueda
            item { SearchBarSection(onSearchClick) }

            // Notificaciones importantes
//            item { NotificationsBanner() }

            // Próxima cita
//            item { NextAppointmentCard(onAppointmentsClick) }

            // Acciones rápidas
            item { QuickActionsSection(onSearchClick, onAppointmentsClick) }

            // Especialidades frecuentes
            item { FrequentSpecialtiesSection() }

            // Promociones
            item { PromotionsSection() }

            // Recordatorios de medicamentos
            item { MedicationRemindersCard() }

            // Doctores destacados
            item {
                Text(
                    "Doctores Destacados",
                    style = typography.titleMedium,
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            items(uiState.featuredDoctors) { doctor ->
                DoctorCard(doctor = doctor, onClick = { onDoctorClick(doctor.id) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarSection(onSearchClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant
        ),
        onClick = onSearchClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant
            )
            Text(
                "Buscar médico, especialidad...",
                style = typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

//@Composable
//private fun NotificationsBanner() {
//    val colorScheme = MaterialTheme.colorScheme
//    val typography = MaterialTheme.typography
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = colorScheme.errorContainer
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                Icons.Default.Warning,
//                contentDescription = null,
//                tint = colorScheme.onErrorContainer,
//                modifier = Modifier.size(24.dp)
//            )
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    "Recordatorio importante",
//                    style = typography.labelLarge,
//                    color = colorScheme.onErrorContainer,
//                    fontWeight = FontWeight.Bold
//                )
//                Text(
//                    "Tienes una cita pendiente mañana",
//                    style = typography.bodySmall,
//                    color = colorScheme.onErrorContainer
//                )
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun NextAppointmentCard(onAppointmentsClick: () -> Unit) {
//    val colorScheme = MaterialTheme.colorScheme
//    val typography = MaterialTheme.typography
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = colorScheme.primaryContainer
//        ),
//        onClick = onAppointmentsClick
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    "Próxima Cita",
//                    style = typography.titleSmall,
//                    color = colorScheme.onPrimaryContainer,
//                    fontWeight = FontWeight.Bold
//                )
//                AssistChip(
//                    onClick = { },
//                    label = { Text("Mañana") },
//                    colors = AssistChipDefaults.assistChipColors(
//                        containerColor = colorScheme.primary,
//                        labelColor = colorScheme.onPrimary
//                    )
//                )
//            }
//
//            HorizontalDivider(color = colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
//
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(50.dp)
//                        .clip(CircleShape)
//                        .background(colorScheme.primary),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        Icons.Default.Person,
//                        contentDescription = null,
//                        tint = colorScheme.onPrimary,
//                        modifier = Modifier.size(30.dp)
//                    )
//                }
//
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        "Dr. Juan Pérez",
//                        style = typography.titleSmall,
//                        color = colorScheme.onPrimaryContainer,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Text(
//                        "Cardiología",
//                        style = typography.bodySmall,
//                        color = colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
//                    )
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(4.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.Schedule,
//                            contentDescription = null,
//                            tint = colorScheme.onPrimaryContainer,
//                            modifier = Modifier.size(16.dp)
//                        )
//                        Text(
//                            "10:00 AM",
//                            style = typography.bodySmall,
//                            color = colorScheme.onPrimaryContainer
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
private fun QuickActionsSection(
    onSearchClick: () -> Unit,
    onAppointmentsClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Accesos Rápidos",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                icon = Icons.Default.Search,
                title = "Buscar",
                color = MaterialTheme.colorScheme.primary,
                onClick = onSearchClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                icon = Icons.Default.CalendarMonth,
                title = "Mis Citas",
                color = MaterialTheme.colorScheme.tertiary,
                onClick = onAppointmentsClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                icon = Icons.Default.MedicalServices,
                title = "Recetas",
                color = MaterialTheme.colorScheme.secondary,
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typography = MaterialTheme.typography

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(28.dp))
            Text(
                title,
                style = typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun FrequentSpecialtiesSection() {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val specialties = listOf(
        "Cardiología" to Icons.Default.Favorite,
        "Pediatría" to Icons.Default.ChildCare,
        "Dermatología" to Icons.Default.Face,
        "Traumatología" to Icons.Default.Healing
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Especialidades Frecuentes",
            style = typography.titleSmall,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(specialties) { (specialty, icon) ->
                SpecialtyChip(specialty, icon)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpecialtyChip(specialty: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.secondaryContainer
        ),
        onClick = { /* TODO */ }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = colorScheme.onSecondaryContainer,
                modifier = Modifier.size(20.dp)
            )
            Text(
                specialty,
                style = MaterialTheme.typography.labelLarge,
                color = colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun PromotionsSection() {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Promociones",
            style = typography.titleSmall,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.tertiaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocalOffer,
                    contentDescription = null,
                    tint = colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(40.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "20% de descuento",
                        style = typography.titleSmall,
                        color = colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Primera consulta de cardiología",
                        style = typography.bodySmall,
                        color = colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@Composable
private fun MedicationRemindersCard() {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Recordatorios de Medicamentos",
            style = typography.titleSmall,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MedicationReminderItem(
                    medication = "Aspirina 100mg",
                    time = "8:00 AM",
                    taken = false
                )
                HorizontalDivider(color = colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                MedicationReminderItem(
                    medication = "Omeprazol 20mg",
                    time = "2:00 PM",
                    taken = true
                )
            }
        }
    }
}

@Composable
private fun MedicationReminderItem(
    medication: String,
    time: String,
    taken: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Medication,
            contentDescription = null,
            tint = if (taken) colorScheme.primary else colorScheme.error,
            modifier = Modifier.size(24.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                medication,
                style = typography.bodyMedium,
                color = colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Text(
                time,
                style = typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }
        if (taken) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Tomado",
                tint = colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        } else {
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text("Marcar", style = typography.labelSmall)
            }
        }
    }
}
