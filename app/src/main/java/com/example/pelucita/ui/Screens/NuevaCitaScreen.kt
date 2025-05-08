package com.example.pelucita.ui.Screens

import android.widget.Toast
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.pelucita.Data.Model.Cita
import com.example.pelucita.Data.Model.Peluquero
import com.example.pelucita.Data.Model.Servicio
import com.example.pelucita.Data.Repository.DBHelper
import com.example.pelucita.Utils.HoraDropdown
import com.example.pelucita.Utils.generarHorasDisponibles
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
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

    // Servios obtenidos de base de datos
    var servicios by remember { mutableStateOf(emptyList<Servicio>()) }
    var expandedServicio by rememberSaveable { mutableStateOf(false) }

    // Peluqueros obtenidos de base de datos
    var peluqueros by remember { mutableStateOf(emptyList<Peluquero>()) }
    var expandedPeluquero by rememberSaveable { mutableStateOf(false) }

    var peluquero by remember { mutableStateOf("") }
    var horasLibres by remember { mutableStateOf(emptyList<String>()) }


    LaunchedEffect(Unit) {
        servicios = dbHelper.obtenerTodosLosServicios()
        peluqueros = dbHelper.obtenerTodosLosPeluqueros()
    }

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

    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {

        Text(
            "🗓️ Reservar nueva cita",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))



        // Boton de fecha mas llamativo
        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (fecha.isEmpty()) "📅 Seleccionar fecha"
                else "📅 Fecha: $fecha",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (horasLibres.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            // Divimos las horas en dos grupos mañanas y tardes
            val (manana, tarde) = horasLibres.partition { it < "15:00" }
            // Divimos las horas en dos grupos mañanas y tardes
            val horasConEtiqueta = manana.map { "🌅 $it" } + tarde.map { "🌇 $it" }

            // El desplegable para mostrar la hora y eligarla
            HoraDropdown(
                label = "Selecciona una hora disponible",
                horas = horasConEtiqueta,                                      // Cogemos la lista con las horas que tiene emoji
                horaSeleccionada = horaSeleccionada,                           // Hora actual selecionada
                onHoraSeleccionada = { horaSeleccionada = it.takeLast(5) } // Solo la hora limpia nos la guardamos
            )

        } else if (fecha.isNotEmpty()) {
            Text(
                "⚠️ No hay horas disponibles",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Dropdown para seleccionar servicio
        ExposedDropdownMenuBox(
            expanded = expandedServicio,
            onExpandedChange = { expandedServicio = !expandedServicio},
            modifier = Modifier.fillMaxWidth()
        ) {
            // Campo de servicio
            OutlinedTextField(
                value = servicio,
                onValueChange = { },
                label = { Text("✂️ Servicio") },
                placeholder = { Text("Ej: Corte de pelo, peinado...") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedServicio) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor().fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
              expanded = expandedServicio,
                onDismissRequest = {expandedServicio=false}
            ){
                servicios.forEach { serv ->
                    DropdownMenuItem(
                        text = { Text(serv.nombre) },
                        onClick = {
                            servicio = serv.nombre
                            expandedServicio = false
                        }
                    )
                }
            }
        }

        // Dropdown para seleccionar peluquero (opcional)

        ExposedDropdownMenuBox(
            expanded = expandedPeluquero,
            onExpandedChange = { expandedPeluquero =! expandedPeluquero },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Campo de peluquero (opcional)
            OutlinedTextField(
                value = peluquero,
                onValueChange = { },
                label = { Text("💇 Peluquero (opcional)") },
                placeholder = { Text("Ej: María, Juan...") },
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPeluquero)},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor().fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expandedPeluquero,
                onDismissRequest = {expandedPeluquero=false}
            ) {
                peluqueros.forEach { pelu ->
                    DropdownMenuItem(
                        text = { Text(pelu.nombre) },
                        onClick = {
                            peluquero = pelu.nombre
                            expandedPeluquero = false
                        }
                    )
                }
            }

        }



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
                    Toast.makeText(context," Cita guardado", Toast.LENGTH_SHORT).show()
                    onCitaGuardada() // Volver atrás una vez guardada la cita
                } else {
                    // Mostrar un mensaje de error si faltan campos
                    Toast.makeText(context, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar cita")
        }
    }
}
