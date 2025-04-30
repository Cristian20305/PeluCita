package com.example.pelucita.ui.Screens

import android.widget.Toast
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.pelucita.Utils.generarHorasDisponibles
import java.util.Calendar

@Composable
fun NuevaCitaScreen(
    clienteId: Int,
    onCitaGuardada: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }

    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var horaSeleccionada by remember { mutableStateOf("") }
    var servicio by remember { mutableStateOf("") }
    var peluquero by remember { mutableStateOf("") }
    var horasLibres by remember { mutableStateOf(emptyList<String>()) }



    // Abrir calendario
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
            fecha = selectedDate

            // cargar horas libres
            val todasLasHoras = generarHorasDisponibles()
            val ocupadas = dbHelper.obtenerCitasPorFecha(selectedDate).map { it.hora }
            horasLibres = todasLasHoras.filterNot { it in ocupadas }
            horaSeleccionada = "" // resetear
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Reservar nueva cita", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))



        Button(onClick = { datePickerDialog.show() }) {
            Text(if (fecha.isEmpty()) "Seleccionar fecha" else "Fecha: $fecha")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (horasLibres.isNotEmpty()) {
            Text("Selecciona una hora", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))

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
                if (fecha.isNotEmpty() && horaSeleccionada.isNotEmpty() && servicio.isNotEmpty()) {
                    val nuevaCita = Cita(
                        id = 0, // Auto-generado en la base de datos
                        clienteId = clienteId,
                        fecha = fecha,
                        hora = horaSeleccionada,
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

@Composable
fun HoraListaRadio(
    horas: List<String>,
    horaSeleccionada: String,
    onHoraSeleccionada: (String) -> Unit
) {
    Column {
        horas.forEach { hora ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = hora == horaSeleccionada,
                        onClick = { onHoraSeleccionada(hora) }
                    )
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = hora == horaSeleccionada,
                    onClick = { onHoraSeleccionada(hora) }
                )
                Text(text = hora)
            }
        }
    }
}