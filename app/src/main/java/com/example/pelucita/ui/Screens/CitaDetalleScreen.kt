package com.example.pelucita.ui.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pelucita.Data.Model.Cita
import com.example.pelucita.Data.Repository.DBHelper

@Composable
fun CitaDetalleScreen(citaId: Int) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }
    var cita by remember { mutableStateOf<Cita?>(null) }

    // Cargar la cita desde la base de datos
    LaunchedEffect(citaId) {
        cita = dbHelper.obtenerCitaPorId(citaId)
    }

    cita?.let {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text("Detalle de Cita", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("📅 Fecha: ${it.fecha}")
            Text("⏰ Hora: ${it.hora}")
            Text("💇 Servicio: ${it.servicio}")
            Text("✂️ Peluquero: ${it.peluquero ?: "No asignado"}")
        }
    } ?: run {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
