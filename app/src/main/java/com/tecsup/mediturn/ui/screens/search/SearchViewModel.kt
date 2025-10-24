package com.tecsup.mediturn.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val doctors: List<Doctor> = emptyList(),
    val selectedFilter: String = "Todos"
)

class SearchViewModel(
    private val repository: DoctorRepository = DoctorRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadDoctors()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchDoctors(query)
    }

    fun onFilterSelected(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    private fun loadDoctors() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val doctors = repository.getAllDoctors()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                doctors = doctors
            )
        }
    }

    private fun searchDoctors(query: String) {
        viewModelScope.launch {
            val doctors = repository.searchDoctors(query)
            _uiState.value = _uiState.value.copy(doctors = doctors)
        }
    }
}