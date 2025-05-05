package com.example.pelucita.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Aquí definimos los colores personalizados para ese toque professional de peluqiería
val LightColors = lightColorScheme(
    primary = Color(0xFFDAA520), // Dorado suave
    onPrimary = Color.White,
    secondary = Color(0xFFFFC1CC), // Rosa claro
    onSecondary = Color.Black,
    surfaceVariant = Color(0xFFF8F8F8), // Fondo de las tarjetas
    background = Color(0xFFFDF5E6), // Fondo general (muy claro)
    error = Color(0xFFB00020),
    onError = Color.White,
    outline = Color(0xFF6D6D6D)
)
val DarkColors = darkColorScheme(
    primary = Color(0xFFFFD700),
    onPrimary = Color.Black,
    secondary = Color(0xFFFFA6B6),
    onSecondary = Color.Black,
    surfaceVariant = Color(0xFF2C2C2C),
    background = Color(0xFF121212),
    error = Color(0xFFFF6F61),
    onError = Color.Black,
    outline = Color(0xFFB0B0B0)
)