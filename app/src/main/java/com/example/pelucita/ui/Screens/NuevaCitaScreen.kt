package com.example.pelucita.ui.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pelucita.Data.Model.Cita
import com.example.pelucita.Data.Repository.DBHelper

@Composable
fun NuevaCitaScreen(
    clienteId: Int,
    onCitaGuardada: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }

    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var servicio by remember { mutableStateOf("") }
    var peluquero by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text("Reservar nueva cita", style = MaterialTheme.typography.titleLarge)

        // Campo de fecha
        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (YYYY-MM-DD)") }
        )

        // Campo de hora
        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (HH:MM)") }
        )

        // Campo de servicio
        OutlinedTextField(
            value = servicio,
            onValueChange = { servicio = it },
            label = { Text("Servicio") }
        )

        // Campo de peluquero (opcional)
        OutlinedTextField(
            value = peluquero,
            onValueChange = { peluquero = it },
            label = { Text("Peluquero (opcional)") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (fecha.isNotEmpty() && hora.isNotEmpty() && servicio.isNotEmpty()) {
                    val nuevaCita = Cita(
                        id = 0, // Auto-generado en la base de datos
                        clienteId = clienteId,
                        fecha = fecha,
                        hora = hora,
                        servicio = servicio,
                        peluquero = if (peluquero.isBlank()) null else peluquero
                    )
                    dbHelper.crearCita(nuevaCita)
                    onCitaGuardada() // Volver atrás una vez guardada la cita
                } else {
                    // Mostrar un mensaje de error si faltan campos
                    Toast.makeText(context, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar cita")
        }
    }
}
