package com.tecsup.mediturn.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

data class ProfileUiState(
    val patient: Patient = Patient(
        id = "1",
        name = "Aldy Montoya",
        email = "aldy.montoya@gmail.com",
        phone = "+51 987 654 321",
        dateOfBirth = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("15/03/2006")!!
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