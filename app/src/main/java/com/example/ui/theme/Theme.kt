package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AIISGColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF00363F),
    primaryContainer = Color(0xFF004E5B),
    onPrimaryContainer = Color(0xFFBCEBFF),
    secondary = CyberGold,
    onSecondary = Color(0xFF422C00),
    secondaryContainer = Color(0xFF5E4000),
    onSecondaryContainer = Color(0xFFFFDEA8),
    tertiary = EmeraldGreen,
    onTertiary = Color(0xFF003822),
    tertiaryContainer = Color(0xFF005234),
    onTertiaryContainer = Color(0xFF99F7BE),
    background = ObsidianBg,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    error = CrimsonAlert,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent cyber theme for AIISG
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = AIISGColorScheme,
        typography = Typography,
        content = content
    )
}
