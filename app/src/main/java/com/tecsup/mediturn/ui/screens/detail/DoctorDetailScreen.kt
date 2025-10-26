package com.tecsup.mediturn.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailScreen(
    doctorId: String,
    onNavigateBack: () -> Unit,
    onBookAppointment: (String) -> Unit,
    viewModel: DoctorDetailViewModel = viewModel()
) {
    LaunchedEffect(doctorId) {
        viewModel.loadDoctor(doctorId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalle del Doctor",
                        style = typography.titleLarge.copy(color = colorScheme.onPrimary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
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
            if (uiState.doctor != null) {
                Surface(
                    shadowElevation = 8.dp,
                    color = colorScheme.surface
                ) {
                    Button(
                        onClick = { onBookAppointment(doctorId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            "Agendar Cita",
                            modifier = Modifier.padding(8.dp),
                            style = typography.labelLarge
                        )
                    }
                }
            }
        },
        containerColor = colorScheme.background
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorScheme.primary)
                }
            }

            uiState.doctor != null -> {
                val doctor = uiState.doctor!!
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 👤 Header
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = doctor.name.split(" ")
                                        .map { it.first() }
                                        .take(2)
                                        .joinToString(""),
                                    style = typography.headlineSmall,
                                    color = colorScheme.onPrimary
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = doctor.name,
                                style = typography.headlineSmall,
                                color = colorScheme.onBackground
                            )
                            Text(
                                text = doctor.specialty.displayName,
                                style = typography.bodyMedium,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // 📊 Stats
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem(
                                emoji = "📅",
                                value = doctor.experience,
                                label = "Experiencia",
                                colorScheme = colorScheme,
                                typography = typography
                            )
                        }
                    }

                    // 📖 About
                    item {
                        InfoCard(
                            title = "Acerca de",
                            content = doctor.about,
                            colorScheme = colorScheme,
                            typography = typography
                        )
                    }

                    // 📍 Info
                    item {
                        InfoCard(
                            title = "Información",
                            colorScheme = colorScheme,
                            typography = typography
                        ) {
                            InfoRow(Icons.Default.LocationOn, doctor.location, colorScheme, typography)
                            InfoRow(
                                Icons.Default.Star,
                                "S/ ${doctor.pricePerConsultation.toInt()} por consulta",
                                colorScheme,
                                typography
                            )
                            if (doctor.isTelehealthAvailable) {
                                InfoRow(
                                    Icons.Default.VideoCall,
                                    "Teleconsulta disponible",
                                    colorScheme,
                                    typography
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: String? = null,
    colorScheme: ColorScheme,
    typography: Typography,
    contentSlot: (@Composable ColumnScope.() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = typography.titleMedium,
                color = colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            if (content != null) {
                Text(
                    text = content,
                    style = typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            } else {
                contentSlot?.invoke(this)
            }
        }
    }
}

@Composable
private fun StatItem(
    emoji: String,
    value: String,
    label: String,
    colorScheme: ColorScheme,
    typography: Typography
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, style = typography.headlineSmall)
        Text(value, style = typography.bodyLarge, color = colorScheme.onBackground)
        Text(label, style = typography.bodySmall, color = colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    colorScheme: ColorScheme,
    typography: Typography
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = colorScheme.primary
        )
        Text(
            text = text,
            style = typography.bodyMedium,
            color = colorScheme.onSurface
        )
    }
}
