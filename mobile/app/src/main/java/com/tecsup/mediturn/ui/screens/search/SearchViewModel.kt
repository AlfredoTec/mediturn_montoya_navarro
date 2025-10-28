package com.tecsup.mediturn.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.model.Specialty
import com.tecsup.mediturn.data.repository.DoctorRepository
import kotlinx.coroutines.flow.*

/**
 * UI State para SearchScreen
 */
data class SearchUiState(
    val searchQuery: String = "",
    val selectedFilter: String = "Todos",
    val doctors: List<Doctor> = emptyList(),
    val selectedSpecialty: Specialty? = null,
    val selectedLocation: String? = null,
    val telehealthOnly: Boolean = false,
    val maxPrice: Double? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel para SearchScreen
 */
class SearchViewModel(
    private val repository: DoctorRepository
) : ViewModel() {

    // Estados de búsqueda
    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow("Todos")
    private val _selectedSpecialty = MutableStateFlow<Specialty?>(null)
    private val _selectedLocation = MutableStateFlow<String?>(null)
    private val _telehealthOnly = MutableStateFlow(false)
    private val _maxPrice = MutableStateFlow<Double?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    // Lista filtrada de doctores
    private val filteredDoctors: StateFlow<List<Doctor>> = combine(
        _searchQuery,
        _selectedSpecialty,
        _selectedLocation,
        _telehealthOnly,
        _maxPrice
    ) { params: Array<Any?> ->
        SearchFilters(
            query = params[0] as String,
            specialty = params[1] as Specialty?,
            location = params[2] as String?,
            telehealthOnly = params[3] as Boolean,
            maxPrice = params[4] as Double?
        )
    }.flatMapLatest { filters ->
        repository.searchDoctorsAdvanced(
            query = filters.query,
            specialty = filters.specialty,
            city = filters.location,
            teleconsultation = if (filters.telehealthOnly) true else null
        ).map { doctors ->
            if (filters.maxPrice != null) {
                doctors.filter { it.pricePerConsultation <= filters.maxPrice }
            } else {
                doctors
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // UI State combinado
    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        _selectedFilter,
        filteredDoctors,
        _selectedSpecialty,
        _selectedLocation,
        _telehealthOnly,
        _maxPrice,
        _isLoading,
        _error
    ) { params: Array<Any?> ->
        SearchUiState(
            searchQuery = params[0] as String,
            selectedFilter = params[1] as String,
            doctors = params[2] as List<Doctor>,
            selectedSpecialty = params[3] as Specialty?,
            selectedLocation = params[4] as String?,
            telehealthOnly = params[5] as Boolean,
            maxPrice = params[6] as Double?,
            isLoading = params[7] as Boolean,
            error = params[8] as String?
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    // Contador de filtros activos
    fun getActiveFiltersCount(): Int {
        var count = 0
        if (_selectedSpecialty.value != null) count++
        if (_selectedLocation.value != null) count++
        if (_telehealthOnly.value) count++
        if (_maxPrice.value != null) count++
        return count
    }

    // Actualizar búsqueda
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    // Seleccionar filtro (para los chips de especialidad)
    fun onFilterSelected(filter: String) {
        _selectedFilter.value = filter

        // Mapear el filtro a una especialidad
        val specialty = when (filter) {
            "Medicina General" -> Specialty.GENERAL_MEDICINE
            "Cardiología" -> Specialty.CARDIOLOGY
            "Pediatría" -> Specialty.PEDIATRICS
            "Dermatología" -> Specialty.DERMATOLOGY
            "Neurología" -> Specialty.NEUROLOGY
            "Traumatología" -> Specialty.ORTHOPEDICS
            else -> null // "Todos"
        }
        _selectedSpecialty.value = specialty
    }

    // Seleccionar especialidad
    fun selectSpecialty(specialty: Specialty?) {
        _selectedSpecialty.value = specialty
    }

    // Seleccionar ubicación
    fun selectLocation(location: String?) {
        _selectedLocation.value = location
    }

    // Toggle teleconsulta
    fun toggleTelehealth() {
        _telehealthOnly.value = !_telehealthOnly.value
    }

    // Establecer precio máximo
    fun setMaxPrice(price: Double?) {
        _maxPrice.value = price
    }

    // Limpiar filtros
    fun clearFilters() {
        _searchQuery.value = ""
        _selectedFilter.value = "Todos"
        _selectedSpecialty.value = null
        _selectedLocation.value = null
        _telehealthOnly.value = false
        _maxPrice.value = null
    }
}

/**
 * Data class para filtros de búsqueda
 */
private data class SearchFilters(
    val query: String,
    val specialty: Specialty?,
    val location: String?,
    val telehealthOnly: Boolean,
    val maxPrice: Double?
)

