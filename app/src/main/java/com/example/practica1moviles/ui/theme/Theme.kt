package com.example.practica1moviles.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldDark,
    onPrimaryContainer = TextPrimary,

    secondary = EmeraldPrimary,
    onSecondary = Color.White,
    secondaryContainer = CardBackground,
    onSecondaryContainer = TextPrimary,

    tertiary = EmeraldPrimary,
    onTertiary = Color.White,
    tertiaryContainer = CardBackground,
    onTertiaryContainer = TextPrimary,

    background = BackgroundBase,
    onBackground = TextPrimary,

    surface = CardBackground,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,

    outline = BorderSubtle,
    outlineVariant = BorderLight,

    error = Color(0xFFEF4444),
    onError = Color.White
)

@Composable
fun PRACTICA1MOVILES22200285Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}