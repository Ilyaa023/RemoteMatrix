package com.remote.matrix.ui.theme

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = PrimaryDark,
    primaryVariant = PrimaryContainerDark,
    secondary = SecondaryDark,
    secondaryVariant = SecondaryContainerDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorDark,
    onPrimary = onPrimaryDark,
    onSecondary = onSecondaryDark,
    onBackground = onBackgroundDark,
    onSurface = onSurfaceDark,
    onError = onErrorDark
)

private val LightColorPalette = lightColors(
    primary = PrimaryLight,
    primaryVariant = PrimaryContainerLight,
    secondary = SecondaryLight,
    secondaryVariant = SecondaryContainerLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    error = ErrorLight,
    onPrimary = onPrimaryLight,
    onSecondary = onSecondaryLight,
    onBackground = onBackgroundLight,
    onSurface = onSurfaceLight,
    onError = onErrorLight
)

@Composable
fun RemoteMatrixTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

//    systemUiController.setStatusBarColor(colors.primarySurface)
    systemUiController.setSystemBarsColor(colors.primaryVariant)

    MaterialTheme(
        colors = colors, typography = Typography, shapes = Shapes, content = content
    )
}

fun Color.Companion.changeAlpha(color: Color, alpha: Float): Color =
    Color(
        red = color.red,
        green = color.green,
        blue = color.blue,
        alpha = if (alpha <= 0f) 0f
                else if (alpha >= 1f) 1f
                else alpha
    )

fun Color.Companion.getGradientColor(color1: Color, color2: Color, level: Float): Color =
    if (level <= 0) color1
    else if (level >= 1) color2
    else {
        Color(
            red = (color2.red * level + color1.red * (1 - level)),
            green = (color2.green * level + color1.green * (1 - level)),
            blue = (color2.blue * level + color1.blue * (1 - level)),
            alpha = (color2.alpha * level + color1.alpha * (1 - level))
        )
    }


