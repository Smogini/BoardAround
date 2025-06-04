package com.boardaround.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGold,
    secondary =  PrimaryBrown,
    tertiary = Errors,
    background =  BackgroundDarkMode,
    surface = MyDarkGray,
    onPrimary = White,
    onSecondary = PrimaryBrown,
    primaryContainer = PrimaryBrown,
    onTertiary = Black,
    onBackground = PrimaryBrown,
    onSurface = White,
    surfaceVariant = News,
    error = Errors
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGold,
    secondary =  PrimaryBrown,
    tertiary = MyDarkGray,
    background =  BackgroundLightMode,
    surface = BottomBar,
    onPrimary = Black,
    onSecondary = White,
    onTertiary = White,
    onBackground = PrimaryBrown,
    onSurface = BlackBrown,
    surfaceVariant = News,
    error = Errors
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
