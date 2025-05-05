package com.example.pelucita.ui.Screens

import android.app.Activity
import android.widget.Toast
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.navigation.NavHostController
import com.example.pelucita.Data.Model.Cita
import com.example.pelucita.Data.Repository.DBHelper
import com.example.pelucita.Utils.generarHorasDisponibles
import java.util.Calendar

@Composable
fun CitaDetalleScreen(citaId: Int, navController: NavHostController) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }
    val activity = LocalContext.current as? Activity

    var cita by remember { mutableStateOf<Cita?>(null) }

    var fecha by remember { mutableStateOf("") }
    var horaSeleccionada by remember { mutableStateOf("") }
    var servicio by remember { mutableStateOf("") }
    var peluquero by remember { mutableStateOf("") }
    var horasLibres by remember { mutableStateOf(emptyList<String>()) }

    // Inicializar calendario
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
            fecha = selectedDate
            val ocupadas = dbHelper.obtenerCitasPorFecha(selectedDate)
                .filter { it.id != cita?.id } // Excluye la cita actual
                .map { it.hora }
            horasLibres = generarHorasDisponibles().filterNot { it in ocupadas }
            horaSeleccionada = "" // reset
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Cargar cita
    LaunchedEffect(citaId) {
        val citaCargada = dbHelper.obtenerCitaPorId(citaId)
        cita = citaCargada
        citaCargada?.let {
            fecha = it.fecha
            horaSeleccionada = it.hora
            servicio = it.servicio
            peluquero = it.peluquero ?: ""
            val ocupadas = dbHelper.obtenerCitasPorFecha(it.fecha)
                .filter { c -> c.id != it.id }
                .map { c -> c.hora }
            horasLibres = generarHorasDisponibles().filterNot { h -> h in ocupadas }
        }
    }

    cita?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Editar Cita", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { datePickerDialog.show() }) {
                Text(if (fecha.isEmpty()) "Seleccionar fecha" else "Fecha: $fecha")
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (horasLibres.isNotEmpty()) {
                Text("Selecciona una hora", style = MaterialTheme.typography.titleMedium)

                val (manana, tarde) = horasLibres.partition { it < "15:00" }

                Text("Turno mañana", style = MaterialTheme.typography.labelMedium)
                HoraListaRadio(horas = manana, horaSeleccionada = horaSeleccionada) {
                    horaSeleccionada = it
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Turno tarde", style = MaterialTheme.typography.labelMedium)
                HoraListaRadio(horas = tarde, horaSeleccionada = horaSeleccionada) {
                    horaSeleccionada = it
                }
            } else if (fecha.isNotEmpty()) {
                Text("No hay horas disponibles", color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = servicio,
                onValueChange = { servicio = it },
                label = { Text("Servicio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = peluquero,
                onValueChange = { peluquero = it },
                label = { Text("Peluquero (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (fecha.isNotEmpty() && horaSeleccionada.isNotEmpty() && servicio.isNotEmpty()) {
                        val citaEditada = it.copy(
                            fecha = fecha,
                            hora = horaSeleccionada,
                            servicio = servicio,
                            peluquero = peluquero.ifBlank { null }
                        )
                        dbHelper.actualizarCita(citaEditada)
                        Toast.makeText(context, "Cita actualizada", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(context, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

