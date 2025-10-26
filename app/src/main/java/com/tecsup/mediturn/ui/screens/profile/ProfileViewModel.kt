package com.tecsup.mediturn.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.model.Patient
import com.tecsup.mediturn.data.model.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val patient: Patient = SampleData.currentPatient,
    val isEditing: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(isEditing = !_uiState.value.isEditing)
    }
}