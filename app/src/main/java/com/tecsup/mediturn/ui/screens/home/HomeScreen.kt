package com.tecsup.mediturn.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.tecsup.mediturn.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onDoctorClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hola, Juan 👋",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "¿Cómo te sientes hoy?",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = Color(0xFF1F2937)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Search Bar
            item {
                MediTurnSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Quick Actions
            item {
                QuickActionsSection(
                    onSearchClick = onNavigateToSearch,
                    onAppointmentsClick = onNavigateToAppointments,
                    onProfileClick = onNavigateToProfile
                )
            }

            // Specialties
            item {
                SpecialtiesSection(
                    onSpecialtyClick = { specialty ->
                        // TODO: Navigate to search with filter
                        onNavigateToSearch()
                    }
                )
            }

            // Upcoming Appointments
            if (uiState.upcomingAppointments.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Próximas Citas",
                        actionText = "Ver todas",
                        onActionClick = onNavigateToAppointments
                    )
                }

                items(uiState.upcomingAppointments.take(2)) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onClick = { /* TODO: Navigate to detail */ }
                    )
                }
            }

            // Featured Doctors
            if (uiState.featuredDoctors.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Médicos Destacados",
                        actionText = "Ver más",
                        onActionClick = onNavigateToSearch
                    )
                }

                items(uiState.featuredDoctors) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        onClick = { onDoctorClick(doctor.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionsSection(
    onSearchClick: () -> Unit,
    onAppointmentsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Accesos Rápidos",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Buscar\nMédicos",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(24.dp)
                    )
                },
                backgroundColor = Color(0xFF2563EB),
                onClick = onSearchClick,
                modifier = Modifier.weight(1f)
            )

            QuickActionCard(
                title = "Mis\nCitas",
                icon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                },
                backgroundColor = Color(0xFF10B981),
                onClick = onAppointmentsClick,
                modifier = Modifier.weight(1f)
            )

            QuickActionCard(
                title = "Mi\nPerfil",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFFFF6B9D),
                        modifier = Modifier.size(24.dp)
                    )
                },
                backgroundColor = Color(0xFFFF6B9D),
                onClick = onProfileClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SpecialtiesSection(
    onSpecialtyClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Especialidades",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val specialties = listOf(
                "Medicina General",
                "Pediatría",
                "Cardiología",
                "Dermatología",
                "Psicología"
            )

            items(specialties) { specialty ->
                SpecialtyChip(
                    specialty = specialty,
                    isSelected = false,
                    onClick = { onSpecialtyClick(specialty) }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )

        TextButton(onClick = onActionClick) {
            Text(
                text = actionText,
                fontSize = 14.sp,
                color = Color(0xFF2563EB)
            )
        }
    }
}