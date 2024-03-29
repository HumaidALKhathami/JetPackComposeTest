package com.example.jetpackcomposetest.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.example.jetpackcomposetest.common.Constants

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val NationalDayColorPalette = darkColors(
    primary = lightGreen,
    primaryVariant = darkGreen,
    secondary = lime
)

@Composable
fun JetPackComposeTestTheme(
    darkTheme: Int = 0,
    content: @Composable () -> Unit
) {
    val colors = when (darkTheme) {
        0 -> {
            LightColorPalette
        }
        1 -> {
            DarkColorPalette
        }
        2 -> {
            NationalDayColorPalette
        }
        else -> {
            LightColorPalette
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}