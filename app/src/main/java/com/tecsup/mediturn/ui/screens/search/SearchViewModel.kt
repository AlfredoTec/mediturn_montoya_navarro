package com.tecsup.mediturn.ui.screens.search

import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.repository.DoctorRepository
import com.tecsup.mediturn.data.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SearchUiState(
    val searchQuery: String = "",
    val selectedFilter: String = "Todos",
    val doctors: List<Doctor> = emptyList()
)

class SearchViewModel : ViewModel() {
    private val doctorRepository = DoctorRepository()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadAllDoctors()
    }

    private fun loadAllDoctors() {
        val doctors = doctorRepository.getAllDoctors()
        _uiState.value = _uiState.value.copy(doctors = doctors)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterDoctors()
    }

    fun onFilterSelected(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        filterDoctors()
    }

    private fun filterDoctors() {
        val allDoctors = doctorRepository.getAllDoctors()
        val filtered = allDoctors.filter { doctor ->
            val matchesSearch = _uiState.value.searchQuery.isEmpty() ||
                    doctor.name.contains(_uiState.value.searchQuery, ignoreCase = true)
            val matchesFilter = _uiState.value.selectedFilter == "Todos" ||
                    doctor.specialty.displayName == _uiState.value.selectedFilter
            matchesSearch && matchesFilter
        }
        _uiState.value = _uiState.value.copy(doctors = filtered)
    }
}