package com.example.rpgolddragon.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Tema Dark (Opção)
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryInk,
    secondary = AccentBlood,
    tertiary = SecondaryParchment,
    background = PrimaryInk,
    onBackground = BackgroundPaper
)

// Tema Light (Foco principal - visual de papel)
private val LightColorScheme = lightColorScheme(
    primary = PrimaryInk,
    secondary = AccentBlood,
    tertiary = SecondaryParchment,
    background = BackgroundPaper,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = PrimaryInk,
    onSurface = PrimaryInk
)

// Tipografia simples (você pode customizar fontes aqui)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    // Adicione outros estilos aqui se necessário
)

@Composable
fun FrontOldDragonTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}