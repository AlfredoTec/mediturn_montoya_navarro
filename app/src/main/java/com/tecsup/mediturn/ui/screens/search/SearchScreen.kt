package com.tecsup.mediturn.ui.screens.search

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.mediturn.MediTurnApplication
import com.tecsup.mediturn.data.model.Specialty
import com.tecsup.mediturn.ui.components.DoctorCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onDoctorClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val app = context.applicationContext as MediTurnApplication

    val viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(app.repository)
    )

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSpecialty by viewModel.selectedSpecialty.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val telehealthOnly by viewModel.telehealthOnly.collectAsState()
    val maxPrice by viewModel.maxPrice.collectAsState()

    val filteredDoctors by viewModel.filteredDoctors.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val availableLocations by viewModel.availableLocations.collectAsState()

    var showFiltersDialog by remember { mutableStateOf(false) }
    val activeFiltersCount = viewModel.getActiveFiltersCount()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Médicos") },
                actions = {
                    // Botón de filtros
                    BadgedBox(
                        badge = {
                            if (activeFiltersCount > 0) {
                                Badge { Text(activeFiltersCount.toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = { showFiltersDialog = true }) {
                            Icon(Icons.Default.FilterList, "Filtros")
                        }
                    }

                    // Botón de limpiar filtros
                    if (activeFiltersCount > 0) {
                        IconButton(onClick = { viewModel.clearFilters() }) {
                            Icon(Icons.Default.Clear, "Limpiar filtros")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar por nombre o especialidad") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, "Limpiar")
                        }
                    }
                },
                singleLine = true
            )

            // Chips de filtros rápidos
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Filtro de telesalud
                item {
                    FilterChip(
                        selected = telehealthOnly,
                        onClick = { viewModel.toggleTelehealth() },
                        label = { Text("Telesalud") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (telehealthOnly) Icons.Default.Check else Icons.Default.Videocam,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }

                // Especialidades
                items(Specialty.getAll()) { specialty ->
                    FilterChip(
                        selected = selectedSpecialty == specialty,
                        onClick = {
                            viewModel.selectSpecialty(
                                if (selectedSpecialty == specialty) null else specialty
                            )
                        },
                        label = { Text(specialty.displayName) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje de error
            error?.let { errorMessage ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            // Resultados
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    filteredDoctors.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "No se encontraron médicos",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Intenta ajustar los filtros",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = "${filteredDoctors.size} médicos encontrados",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            items(filteredDoctors) { doctor ->
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

        // Diálogo de filtros avanzados
        if (showFiltersDialog) {
            FiltersDialog(
                selectedLocation = selectedLocation,
                availableLocations = availableLocations,
                maxPrice = maxPrice,
                onLocationSelected = { viewModel.selectLocation(it) },
                onMaxPriceChanged = { viewModel.setMaxPrice(it) },
                onDismiss = { showFiltersDialog = false },
                onApply = { showFiltersDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersDialog(
    selectedLocation: String,
    availableLocations: List<String>,
    maxPrice: Double?,
    onLocationSelected: (String) -> Unit,
    onMaxPriceChanged: (Double?) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    var expandedLocation by remember { mutableStateOf(false) }
    var priceText by remember { mutableStateOf(maxPrice?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtros Avanzados") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Filtro de ubicación
                ExposedDropdownMenuBox(
                    expanded = expandedLocation,
                    onExpandedChange = { expandedLocation = it }
                ) {
                    OutlinedTextField(
                        value = selectedLocation.ifEmpty { "Todas las ubicaciones" },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ubicación") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocation)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedLocation,
                        onDismissRequest = { expandedLocation = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas las ubicaciones") },
                            onClick = {
                                onLocationSelected("")
                                expandedLocation = false
                            }
                        )
                        availableLocations.forEach { location ->
                            DropdownMenuItem(
                                text = { Text(location) },
                                onClick = {
                                    onLocationSelected(location)
                                    expandedLocation = false
                                }
                            )
                        }
                    }
                }

                // Filtro de precio máximo
                OutlinedTextField(
                    value = priceText,
                    onValueChange = {
                        priceText = it
                        val price = it.toDoubleOrNull()
                        onMaxPriceChanged(price)
                    },
                    label = { Text("Precio máximo (S/)") },
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (maxPrice != null) {
                    Text(
                        text = "Mostrando médicos hasta S/ $maxPrice",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onApply) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}