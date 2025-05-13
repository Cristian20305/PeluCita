package com.example.pelucita.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.navigation.NavHostController
import com.example.pelucita.Data.model.Cita
import com.example.pelucita.Data.model.Peluquero
import com.example.pelucita.Data.model.Servicio
import com.example.pelucita.Data.repository.DBHelper
import com.example.pelucita.Utils.HoraDropdown
import com.example.pelucita.Utils.añadirCitaAlCalendario
import com.example.pelucita.Utils.generarHorasDisponibles
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaDetalleScreen(citaId: Int, navController: NavHostController) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }
    val activity = LocalContext.current as? Activity


    // Servios obtenidos de base de datos
    var servicios by remember { mutableStateOf(emptyList<Servicio>()) }
    var expandedServicio by rememberSaveable { mutableStateOf(false) }

    // Peluqueros obtenidos de base de datos
    var peluqueros by remember { mutableStateOf(emptyList<Peluquero>()) }
    var expandedPeluquero by rememberSaveable { mutableStateOf(false) }

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
            Text(
                text = "✏️ Editar Cita",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (fecha.isEmpty()) "📅 Seleccionar fecha"
                    else "📅 Fecha: $fecha",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (horasLibres.isNotEmpty()) {
                // Divimos las horas en dos grupos mañanas y tardes
                val (manana, tarde) = horasLibres.partition { it < "15:00" }
                // Ponemos emoji para saber si es mañana o tarde
                val horasConEtiqueta = manana.map { "🌅 $it" } + tarde.map { "🌇 $it" }

                // El desplegable para mostrar la hora y eligarla
                HoraDropdown(
                    label = "Selecciona una hora disponible",
                    horas = horasConEtiqueta,                                      // Cogemos la lista con las horas que tiene emoji
                    horaSeleccionada = horaSeleccionada,                           // Hora actual selecionada
                    onHoraSeleccionada = {
                        horaSeleccionada = it.takeLast(5)
                    }  // Solo la hora limpia nos la guardamos
                )

            } else if (fecha.isNotEmpty()) {
                Text(
                    "⚠️ No hay horas disponibles",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown para seleccionar servicio
            ExposedDropdownMenuBox(
                expanded = expandedServicio,
                onExpandedChange = { expandedServicio = !expandedServicio },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Escribimos el servicio que queremos o necesitamos
                OutlinedTextField(
                    value = servicio,
                    onValueChange = {},
                    label = { Text("✂️ Servicio") },
                    placeholder = { Text("Ej: Corte de pelo, peinado...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedServicio,
                    onDismissRequest = { expandedServicio = false }
                ) {
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
                onExpandedChange = { expandedPeluquero = !expandedPeluquero },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Texto para el nombre del peluquero
                OutlinedTextField(
                    value = peluquero,
                    onValueChange = { peluquero = it },
                    label = { Text("💇 Peluquero (opcional)") },
                    placeholder = { Text("Ej: María, Juan...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedPeluquero,
                    onDismissRequest = { expandedPeluquero = false }
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
                        val citaEditada = it.copy(
                            fecha = fecha,
                            hora = horaSeleccionada,
                            servicio = servicio,
                            peluquero = peluquero.ifBlank { null }
                        )
                        dbHelper.actualizarCita(citaEditada)
                        Toast.makeText(context, "Cita actualizada", Toast.LENGTH_SHORT).show()
                        // Volvemos atras al actualizar
                        navController.popBackStack()

                    } else {
                        Toast.makeText(
                            context,
                            "Completa todos los campos obligatorios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar cambios")
            }
            Button(
                onClick = {
                    añadirCitaAlCalendario(context, cita!!)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)

            ) {
                Text("📆 Añadir al calendario")
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

