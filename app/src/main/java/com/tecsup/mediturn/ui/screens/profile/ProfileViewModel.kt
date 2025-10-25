package com.tecsup.mediturn.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val patient: Patient = Patient(
        id = "patient_1",
        name = "Juan Pérez",
        email = "juan.perez@email.com",
        phone = "+51 987654321",
        dateOfBirth = "15/03/1990"
    ),
    val isEditing: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(isEditing = !_uiState.value.isEditing)
    }
}