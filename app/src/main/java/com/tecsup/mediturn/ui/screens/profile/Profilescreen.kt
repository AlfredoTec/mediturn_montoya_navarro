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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = colors.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = colors.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleEditMode() }) {
                        Icon(
                            if (uiState.isEditing) Icons.Default.Check else Icons.Default.Edit,
                            if (uiState.isEditing) "Guardar" else "Editar",
                            tint = colors.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.surface
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
            // Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(colors.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.patient.name.split(" ")
                                .map { it.first() }
                                .take(2)
                                .joinToString(""),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.onPrimary
                        )
                    }
                    Text(
                        uiState.patient.name,
                        style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = colors.onSurface
                    )
                }
            }

            // Personal Info
            item {
                Text(
                    "Información Personal",
                    style = typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.onSurface
                )
            }

            item {
                ProfileInfoCard(
                    Icons.Default.Email,
                    "Email",
                    uiState.patient.email,
                    uiState.isEditing
                )
            }

            item {
                ProfileInfoCard(
                    Icons.Default.Phone,
                    "Teléfono",
                    uiState.patient.phone.ifEmpty { "No registrado" },
                    uiState.isEditing
                )
            }

            item {
                ProfileInfoCard(
                    Icons.Default.CalendarToday,
                    "Fecha Nacimiento",
                    dateFormatter.format(uiState.patient.dateOfBirth)
                        .ifEmpty { "No registrado" },
                    uiState.isEditing
                )
            }

            // Settings section
            item {
                Text(
                    "Configuración",
                    style = typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.onSurface
                )
            }

            // Tema oscuro/claro
            item {
                SettingsOption(
                    icon = Icons.Default.DarkMode,
                    title = "Modo oscuro",
                    subtitle = if (isDarkMode) "Activado" else "Desactivado",
                    trailingContent = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onToggleTheme() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colors.surface,
                                checkedTrackColor = colors.secondary
                            )
                        )
                    }
                )
            }
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
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(24.dp), tint = colors.onSurfaceVariant)
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = typography.labelSmall, color = colors.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                if (isEditing) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.surfaceVariant
                        )
                    )
                } else {
                    Text(
                        value,
                        style = typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = colors.onSurface
                    )
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
    subtitle: String,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = {},
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(24.dp), tint = colors.onSurfaceVariant)
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = typography.bodyLarge, color = colors.onSurface)
                Text(subtitle, style = typography.labelSmall, color = colors.onSurfaceVariant)
            }
            trailingContent?.invoke()
        }
    }
}
