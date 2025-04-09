package com.example.pelucita.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CitaDetalleScreen(citaId: Int) {
    Column {
        Text("Detalle de la cita con ID: $citaId")
    }
}