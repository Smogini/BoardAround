package com.boardaround.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFC107),
    secondary =  Color(0xFF3E2723),
    tertiary = Color.Red,
    background =  Color(0xFF121212), // Sfondo scuro
    surface = Color(0xFF1E1E1E),    // Superficie scura
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color(0xFF3E2723),
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFC107),
    secondary =  Color(0xFF3E2723),
    tertiary = Color.Red,
    background =  Color(0xFFEDE0D4),
    surface = Color(0xFF5D4037),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF3E2723),
    onSurface = Color(0xFF1C1B1F),
)

val LocalIsDarkMode = staticCompositionLocalOf { false }

@Composable
fun BoardAroundTheme(
    isDarkMode: Boolean, // <--- aggiunto parametro
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalIsDarkMode provides isDarkMode) {
        val colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}
