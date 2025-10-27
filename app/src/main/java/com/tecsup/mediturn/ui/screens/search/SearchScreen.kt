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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.mediturn.ui.components.DoctorCard
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onDoctorClick: (String) -> Unit,
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: SearchViewModel = viewModel(factory = viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Estado para controlar el diálogo de filtros avanzados
    var showAdvancedFilters by remember { mutableStateOf(false) }

    // Mostrar diálogo si el usuario lo solicita
    if (showAdvancedFilters) {
        AdvancedFiltersDialog(
            currentLocation = uiState.selectedLocation,
            currentTelehealthOnly = uiState.telehealthOnly,
            currentMaxPrice = uiState.maxPrice,
            onDismiss = { showAdvancedFilters = false },
            onApply = { location, telehealth, price ->
                // Aplicar filtros al ViewModel
                viewModel.selectLocation(location)
                if (telehealth != uiState.telehealthOnly) {
                    viewModel.toggleTelehealth()
                }
                viewModel.setMaxPrice(price)
                showAdvancedFilters = false
            },
            onClear = {
                // Limpiar todos los filtros
                viewModel.clearFilters()
                showAdvancedFilters = false
            }
        )
    }

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

            // Fila con chips de especialidad y botón de filtros avanzados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chips de especialidad (toma la mayor parte del espacio)
                Box(modifier = Modifier.weight(1f)) {
                    FilterSection(
                        selectedFilter = uiState.selectedFilter,
                        onFilterSelected = { viewModel.onFilterSelected(it) }
                    )
                }

                // Botón de filtros avanzados con badge si hay filtros activos
                BadgedBox(
                    badge = {
                        val activeFilters = viewModel.getActiveFiltersCount()
                        if (activeFilters > 0) {
                            Badge { Text("$activeFilters") }
                        }
                    }
                ) {
                    IconButton(onClick = { showAdvancedFilters = true }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filtros avanzados",
                            tint = colors.primary
                        )
                    }
                }
            }

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

/**
 * Diálogo de filtros avanzados
 * Permite filtrar doctores por ciudad, teleconsulta y precio máximo
 */
@Composable
private fun AdvancedFiltersDialog(
    currentLocation: String?,
    currentTelehealthOnly: Boolean,
    currentMaxPrice: Double?,
    onDismiss: () -> Unit,
    onApply: (location: String?, telehealthOnly: Boolean, maxPrice: Double?) -> Unit,
    onClear: () -> Unit
) {
    // Estados locales para los filtros antes de aplicarlos
    var location by remember { mutableStateOf(currentLocation ?: "") }
    var telehealthOnly by remember { mutableStateOf(currentTelehealthOnly) }
    var maxPrice by remember { mutableStateOf(currentMaxPrice ?: 200.0) }
    var usePriceFilter by remember { mutableStateOf(currentMaxPrice != null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título del diálogo
                Text(
                    text = "Filtros Avanzados",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Divider()

                // Dropdown de ciudades - Filtra doctores por ubicación disponible en los datos
                Column {
                    Text(
                        text = "Ubicación",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Lista de ciudades/distritos disponibles en Lima
                    val availableLocations = listOf(
                        "Todas",
                        "Surco",
                        "San Isidro",
                        "Jesús María",
                        "Miraflores"
                    )

                    // Chips horizontales para seleccionar ubicación
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableLocations) { loc ->
                            FilterChip(
                                selected = if (loc == "Todas") location.isEmpty() else location.contains(loc, ignoreCase = true),
                                onClick = {
                                    location = if (loc == "Todas") "" else loc
                                },
                                label = { Text(loc) }
                            )
                        }
                    }
                }

                // Switch para teleconsulta - Muestra solo doctores que ofrecen consulta virtual
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.VideoCall,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Solo Teleconsulta",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Switch(
                        checked = telehealthOnly,
                        onCheckedChange = { telehealthOnly = it }
                    )
                }

                // Checkbox para activar filtro de precio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtrar por precio",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Checkbox(
                        checked = usePriceFilter,
                        onCheckedChange = { usePriceFilter = it }
                    )
                }

                // Slider de precio máximo - Filtra doctores con precio menor o igual al seleccionado
                if (usePriceFilter) {
                    Column {
                        Text(
                            text = "Precio máximo: S/ ${maxPrice.roundToInt()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Slider(
                            value = maxPrice.toFloat(),
                            onValueChange = { maxPrice = it.toDouble() },
                            valueRange = 50f..500f,
                            steps = 17 // Pasos de 25 en 25
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("S/ 50", style = MaterialTheme.typography.labelSmall)
                            Text("S/ 500", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                Divider()

                // Botones de acción - Centrados con peso equitativo
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Primera fila: Limpiar
                    OutlinedButton(
                        onClick = onClear,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limpiar Filtros")
                    }

                    // Segunda fila: Cancelar y Aplicar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Botón para cancelar sin aplicar cambios
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }

                        // Botón para aplicar los filtros seleccionados
                        Button(
                            onClick = {
                                onApply(
                                    if (location.isBlank()) null else location,
                                    telehealthOnly,
                                    if (usePriceFilter) maxPrice else null
                                )
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Aplicar")
                        }
                    }
                }
            }
        }
    }
}
