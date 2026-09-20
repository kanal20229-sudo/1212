package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val UkrColorScheme = darkColorScheme(
    primary = UkrFlagYellow,
    onPrimary = UkrTextOnYellow,
    primaryContainer = UkrNavyCardElevated,
    onPrimaryContainer = UkrFlagYellowLight,
    secondary = UkrFlagBlue,
    onSecondary = UkrTextPrimary,
    secondaryContainer = UkrNavyBorder,
    onSecondaryContainer = UkrFlagBlueLight,
    tertiary = UkrGreenSuccess,
    onTertiary = UkrNavyBackground,
    background = UkrNavyBackground,
    onBackground = UkrTextPrimary,
    surface = UkrNavySurface,
    onSurface = UkrTextPrimary,
    surfaceVariant = UkrNavyCard,
    onSurfaceVariant = UkrTextSecondary,
    outline = UkrNavyBorder,
    error = UkrRedUrgent,
    onError = UkrTextPrimary
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = UkrColorScheme,
        typography = Typography,
        content = content
    )
}
