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
    val doctors: List<Doctor> = emptyList(),
    val isLoading: Boolean = false
)

class SearchViewModel : ViewModel() {

    private val doctorRepository = DoctorRepository()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadDoctors()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchDoctors()
    }

    fun onFilterSelected(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        filterDoctors()
    }

    private fun loadDoctors() {
        _uiState.value = _uiState.value.copy(
            doctors = doctorRepository.getAllDoctors()
        )
    }

    private fun searchDoctors() {
        val query = _uiState.value.searchQuery
        val doctors = if (query.isBlank()) {
            doctorRepository.getAllDoctors()
        } else {
            doctorRepository.searchByName(query)
        }
        _uiState.value = _uiState.value.copy(doctors = doctors)
    }

    private fun filterDoctors() {
        val filter = _uiState.value.selectedFilter
        val doctors = doctorRepository.searchBySpecialty(filter)
        _uiState.value = _uiState.value.copy(doctors = doctors)
    }
}