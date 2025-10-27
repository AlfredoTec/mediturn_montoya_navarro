package com.tecsup.mediturn.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.mediturn.ui.components.DoctorCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onDoctorClick: (String) -> Unit,
    viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory()
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Buscar Doctores",
                        style = typography.titleLarge.copy(color = colors.onSurface)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = colors.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.surface
                )
            )
        },
        containerColor = colors.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 🔍 Search Bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por nombre...", color = colors.onSurfaceVariant) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = colors.primary)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    cursorColor = colors.primary,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🩺 Filters
            FilterSection(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = { viewModel.onFilterSelected(it) }
            )

            // 📋 Results
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.doctors.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No se encontraron doctores",
                                color = colors.onSurfaceVariant,
                                style = typography.bodyMedium
                            )
                        }
                    }
                } else {
                    items(uiState.doctors) { doctor ->
                        DoctorCard(
                            doctor = doctor,
                            onClick = { onDoctorClick(doctor.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf(
        "Todos", "Medicina General", "Cardiología",
        "Pediatría", "Dermatología", "Neurología", "Traumatología"
    )
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    LazyRow(
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                filter = filter,
                isSelected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                colors = colors,
                typography = typography
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChip(
    filter: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: ColorScheme,
    typography: Typography
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colors.primary else colors.surfaceVariant
        )
    ) {
        Text(
            text = filter,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = typography.labelLarge.copy(
                color = if (isSelected) colors.onPrimary else colors.onSurface
            )
        )
    }
}
