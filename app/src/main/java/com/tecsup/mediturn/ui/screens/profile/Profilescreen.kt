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
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleEditMode() }) {
                        Icon(
                            if (uiState.isEditing) Icons.Default.Check else Icons.Default.Edit,
                            if (uiState.isEditing) "Guardar" else "Editar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.patient.name.split(" ").map { it.first() }.take(2).joinToString(""),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(uiState.patient.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Personal Info
            item {
                Text("Información Personal", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            item { ProfileInfoCard(Icons.Default.Email, "Email", uiState.patient.email, uiState.isEditing) }
            item { ProfileInfoCard(Icons.Default.Phone, "Teléfono", uiState.patient.phone.ifEmpty { "No registrado" }, uiState.isEditing) }
            item { ProfileInfoCard(Icons.Default.CalendarToday, "Fecha Nacimiento", uiState.patient.dateOfBirth.ifEmpty { "No registrado" }, uiState.isEditing) }

            // Settings
            item {
                Text("Configuración", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            item { SettingsOption(Icons.Default.Notifications, "Notificaciones", "Gestionar recordatorios") }
            item { SettingsOption(Icons.Default.Lock, "Privacidad", "Configuración de privacidad") }
            item { SettingsOption(Icons.Default.Info, "Acerca de", "MediTurn v1.0") }
        }
    }
}

@Composable
private fun ProfileInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isEditing: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(24.dp), tint = Color(0xFF2563EB))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontSize = 12.sp, color = Color(0xFF6B7280))
                Spacer(Modifier.height(4.dp))
                if (isEditing) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2563EB),
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )
                } else {
                    Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = {}
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(24.dp), tint = Color(0xFF6B7280))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(subtitle, fontSize = 12.sp, color = Color(0xFF6B7280))
            }
            Icon(Icons.Default.KeyboardArrowRight, null, tint = Color(0xFF9CA3AF))
        }
    }
}