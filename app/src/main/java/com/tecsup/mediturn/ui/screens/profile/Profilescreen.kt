package com.tecsup.mediturn.ui.screens.profile

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleEditMode() }) {
                        Icon(
                            imageVector = if (uiState.isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (uiState.isEditing) "Guardar" else "Editar"
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
            // Profile Header
            item {
                ProfileHeader(patient = uiState.patient)
            }

            // Personal Information
            item {
                SectionTitle(title = "Información Personal")
            }

            item {
                ProfileInfoCard(
                    icon = Icons.Default.Email,
                    label = "Correo electrónico",
                    value = uiState.patient.email,
                    isEditing = uiState.isEditing
                )
            }

            item {
                ProfileInfoCard(
                    icon = Icons.Default.Phone,
                    label = "Teléfono",
                    value = uiState.patient.phone,
                    isEditing = uiState.isEditing
                )
            }

            item {
                ProfileInfoCard(
                    icon = Icons.Default.CalendarToday,
                    label = "Fecha de nacimiento",
                    value = uiState.patient.dateOfBirth,
                    isEditing = uiState.isEditing
                )
            }

            // Medical Information
            item {
                SectionTitle(title = "Información Médica")
            }

            item {
                ProfileInfoCard(
                    icon = Icons.Default.Favorite,
                    label = "Tipo de sangre",
                    value = uiState.patient.bloodType,
                    isEditing = uiState.isEditing
                )
            }

            item {
                ProfileInfoCard(
                    icon = Icons.Default.Warning,
                    label = "Alergias",
                    value = uiState.patient.allergies.joinToString(", "),
                    isEditing = uiState.isEditing
                )
            }

            // Emergency Contact
            item {
                SectionTitle(title = "Contacto de Emergencia")
            }

            item {
                ProfileInfoCard(
                    icon = Icons.Default.Person,
                    label = "Contacto",
                    value = uiState.patient.emergencyContact,
                    isEditing = uiState.isEditing
                )
            }

            // Settings Options
            item {
                SectionTitle(title = "Configuración")
            }

            item {
                SettingsOption(
                    icon = Icons.Default.Notifications,
                    title = "Notificaciones",
                    subtitle = "Gestionar recordatorios"
                )
            }

            item {
                SettingsOption(
                    icon = Icons.Default.Lock,
                    title = "Privacidad",
                    subtitle = "Configuración de privacidad"
                )
            }

            item {
                SettingsOption(
                    icon = Icons.Default.Info,
                    title = "Acerca de",
                    subtitle = "Versión 1.0.0"
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(patient: com.tecsup.mediturn.data.model.Patient) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF2563EB)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = patient.name.split(" ").map { it.first() }.take(2).joinToString(""),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Name
        Text(
            text = patient.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF1F2937)
    )
}

@Composable
private fun ProfileInfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    isEditing: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9FAFB)
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
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
                Spacer(modifier = Modifier.height(4.dp))

                if (isEditing) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = { /* Handle change */ },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2563EB),
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )
                } else {
                    Text(
                        text = value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1F2937)
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsOption(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = { /* Handle click */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9CA3AF)
            )
        }
    }
}