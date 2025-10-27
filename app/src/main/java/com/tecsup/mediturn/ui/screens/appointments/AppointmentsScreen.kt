package com.tecsup.mediturn.ui.screens.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.mediturn.ui.components.AppointmentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AppointmentsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis Citas",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = uiState.selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) },
                    text = {
                        Text(
                            text = "Próximas",
                            color = if (uiState.selectedTab == 0)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) },
                    text = {
                        Text(
                            text = "Pasadas",
                            color = if (uiState.selectedTab == 1)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }

            // Content
            val appointments = if (uiState.selectedTab == 0)
                uiState.upcomingAppointments
            else
                uiState.pastAppointments

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (appointments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (uiState.selectedTab == 0)
                                    "No tienes citas próximas"
                                else
                                    "No tienes citas pasadas",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    items(appointments) { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            onCancelClick = if (uiState.selectedTab == 0) {
                                { viewModel.cancelAppointment(appointment.id) }
                            } else null
                        )
                    }
                }
            }
        }
    }
}
