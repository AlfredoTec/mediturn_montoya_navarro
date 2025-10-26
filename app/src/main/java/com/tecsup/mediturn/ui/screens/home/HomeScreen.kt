package com.tecsup.mediturn.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    viewModel: HomeViewModel = viewModel()
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { SearchBarSection(onSearchClick) }
            item { QuickActionsSection(onSearchClick, onAppointmentsClick) }

            item {
                Text(
                    "Doctores Destacados",
                    style = typography.titleMedium,
                    color = colorScheme.onSurface
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

@Composable
private fun QuickActionsSection(
    onSearchClick: () -> Unit,
    onAppointmentsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionCard(
            icon = Icons.Default.Search,
            title = "Buscar Doctor",
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(32.dp))
            Text(
                title,
                style = typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
