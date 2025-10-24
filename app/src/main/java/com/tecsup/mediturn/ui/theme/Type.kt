package com.tecsup.mediturn.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = BackgroundLight,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = Gray900,

    secondary = PrimaryGreen,
    onSecondary = BackgroundLight,
    secondaryContainer = PrimaryGreenLight,
    onSecondaryContainer = Gray900,

    tertiary = SecondaryTurquoise,
    onTertiary = BackgroundLight,

    background = BackgroundLight,
    onBackground = Gray900,

    surface = BackgroundLight,
    onSurface = Gray900,
    surfaceVariant = Gray50,
    onSurfaceVariant = Gray600,

    error = StatusError,
    onError = BackgroundLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = Gray900,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = BackgroundLight,

    secondary = PrimaryGreenLight,
    onSecondary = Gray900,
    secondaryContainer = PrimaryGreenDark,
    onSecondaryContainer = BackgroundLight,

    background = Gray900,
    onBackground = BackgroundLight,

    surface = Gray800,
    onSurface = BackgroundLight,
    surfaceVariant = Gray600,
    onSurfaceVariant = Gray200
)

@Composable
fun MediTurnTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}