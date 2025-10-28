package com.tecsup.mediturn.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.ThemePreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val prefs: ThemePreferenceManager
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    init {
        // Cargar valor inicial desde DataStore
        viewModelScope.launch {
            prefs.isDarkMode.collect { value ->
                _isDarkMode.value = value
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newValue = !_isDarkMode.value
            prefs.setDarkMode(newValue)
        }
    }
}
