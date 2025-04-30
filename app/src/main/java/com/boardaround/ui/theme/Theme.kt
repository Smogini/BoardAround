package com.boardaround.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGold,
    secondary =  PrimaryBrown,
    tertiary = Errors,
    background =  Background,
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = PrimaryBrown,
    primaryContainer = PrimaryBrown,
    onTertiary = Color.White,
    onBackground = PrimaryBrown,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGold,
    secondary =  PrimaryBrown,
    tertiary = Errors,
    background =  Color(0xFFEDE0D4),
    surface = Color(0xFF5D4037),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = PrimaryBrown,
    onSurface = Color(0xFF1C1B1F),
)

val LocalIsDarkMode = staticCompositionLocalOf { false }

@Composable
fun BoardAroundTheme(
    isDarkMode: Boolean,
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
