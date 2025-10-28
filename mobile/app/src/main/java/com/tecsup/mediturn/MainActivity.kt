package com.tecsup.mediturn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tecsup.mediturn.data.ThemePreferenceManager
import com.tecsup.mediturn.navigation.NavGraph
import com.tecsup.mediturn.ui.theme.MediTurnTheme
import com.tecsup.mediturn.ui.theme.ThemeViewModel
import com.tecsup.mediturn.ui.theme.ThemeViewModelFactory
import com.tecsup.mediturn.ui.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = ThemePreferenceManager(this)
        val app = application as MediTurnApplication
        val viewModelFactory = ViewModelFactory(app)

        setContent {
            val themeViewModel: ThemeViewModel = viewModel(
                factory = ThemeViewModelFactory(prefs)
            )

            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            MediTurnTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavGraph(
                        navController = navController,
                        isDarkMode = isDarkMode,
                        onToggleTheme = { themeViewModel.toggleTheme() },
                        viewModelFactory = viewModelFactory
                    )
                }
            }
        }
    }
}
