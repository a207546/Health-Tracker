package com.example.a207546_huangwenchen_cikguizwan

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
//creates a custom Material 3 theme. Applies the colors I defined to the whole app.
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    background = Color(0xFFF5F7FA),
    surface = Color.White,
    onSurface = Color(0xFF212121)
)

@Composable
fun HealthTrackerTheme(
    darkMode: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkMode) DarkColorScheme else LightColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}