package com.idimi.mastermindgame.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    secondary = CardNavy,
    background = DeepSpace,
    surface = CardNavy,
    onPrimary = Color.Black,
    onPrimaryContainer = Color.White,
    tertiary = NeonGreen,
    secondaryContainer = NeonOrange,
    onBackground = Color.White,
    onSurface = Color.White,
    error = NeonRed
)

private val LightColorScheme = lightColorScheme(
    primary = PowerGreen,
    secondary = RetroBlue,
    background = NeonGreen,
    surface = RetroBlue,
    onPrimary = Color.White,
    onPrimaryContainer = Color.Black,
    tertiary = NeonGreen,
    secondaryContainer = NeonOrange,
    onBackground = gray,
    onSurface = gray,
    error = NeonRed
)

@Composable
fun MastermindGameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.navigationBarColor = colorScheme.background.toArgb()
            window.statusBarColor = colorScheme.background.toArgb()
            window.isNavigationBarContrastEnforced = true

            val windowInsetsController = WindowCompat.getInsetsController(window, view)

            if (darkTheme) {
                windowInsetsController.isAppearanceLightNavigationBars = false
                windowInsetsController.isAppearanceLightStatusBars = false
            } else {
                windowInsetsController.isAppearanceLightNavigationBars = true
                windowInsetsController.isAppearanceLightStatusBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
