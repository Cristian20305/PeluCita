import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.example.pelucita.Data.model.Cita
import com.example.pelucita.Data.model.Usuario
import com.example.pelucita.Data.repository.DBHelper
import com.example.pelucita.Navigation.CitaDetalleRoute
import com.example.pelucita.Navigation.NuevaCitaScreenRoute
import java.util.*

@Composable
fun AdminHomeScreen(navController: NavController) {
    // Obtenemos el contexto y la base de datos
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }

    // Variable para guardar la fecha que el admin selecciona
    var fechaSeleccionada by remember { mutableStateOf("") }

    // Lista de citas del día seleccionado
    var citas by remember { mutableStateOf(emptyList<Cita>()) }

    // Guardamos la cita a eliminar temporalmente (para el AlertDialog)
    var citaAEliminar by remember { mutableStateOf<Cita?>(null) }

    // Lista de clientes para el diálogo de selección
    var clientes by remember { mutableStateOf(emptyList<Usuario>()) }

    // Mostrar diálogo para elegir cliente
    var mostrarDialogoClientes by remember { mutableStateOf(false) }

    // Campo de búsqueda para filtrar citas
    var textoBusqueda by remember { mutableStateOf("") }

    // Citas filtradas por servicio y peluquero
    val citasFiltradas = if (textoBusqueda.isBlank()) citas else
        citas.filter {
            it.servicio.contains(textoBusqueda, ignoreCase = true) ||
                    it.peluquero?.contains(textoBusqueda, ignoreCase = true) == true
        }

    // Objeto calendario para el DatePicker
    val calendar = Calendar.getInstance()

    // Creamos el selector de fecha (DatePickerDialog)
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Cuando selecciona fecha, la guardamos con el formato adecuado
            fechaSeleccionada = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
            // Buscamos en la base de datos las citas de ese día
            citas = dbHelper.obtenerCitasPorFecha(fechaSeleccionada)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Estructura visual principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título de la pantalla
        Text(
            "📋 Gestión de citas",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para abrir el calendario y seleccionar fecha
        Button(
            onClick = { datePicker.show() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (fechaSeleccionada.isEmpty()) "Seleccionar fecha"
                else "📅 Fecha seleccionada: $fechaSeleccionada"
            )
        }



        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            label = { Text("🔍 Buscar por servicio o peluquero") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))


        Spacer(modifier = Modifier.height(16.dp))

        // Botón para añadir nueva cita como admin
        Button(
            onClick = {
                // Al pulsar, cargamos los clientes y mostramos el diálogo
                clientes = dbHelper.obtenerTodosLosUsuarios()
                mostrarDialogoClientes = true
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("➕ Agendar nueva cita")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostramos las citas del día si hay una fecha seleccionada
        if (fechaSeleccionada.isNotEmpty()) {
            if (citas.isEmpty()) {
                // Si no hay citas, mensaje informativo
                Text(
                    "No se han encontrado citas para la fecha seleccionada.",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                // Lista de citas en ese día con animacion para que quede mas bonito
                AnimatedVisibility(visible = citasFiltradas.isNotEmpty()) {
                    LazyColumn {
                        items(citasFiltradas) { cita ->
                            // Tarjeta con los datos de la cita
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                                shape = RoundedCornerShape(16.dp),
                                // Aplicamos colores personalizados desde el theme
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Schedule, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Hora: ${cita.hora}")
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.ContentCut, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Servicio: ${cita.servicio}")
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Person, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Peluquero: ${cita.peluquero ?: "No asignado"}")
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("ID Cliente: ${cita.clienteId}")

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Botones en fila: Editar y Eliminar
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Botón para editar la cita
                                        Button(
                                            onClick = {
                                                // Navegamos a la pantalla de detalle para editar la cita
                                                navController.navigate(CitaDetalleRoute(cita.id))
                                            },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("Editar cita")
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        // Botón para eliminar con confirmación
                                        Button(
                                            onClick = { citaAEliminar = cita },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                            ),
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(
                                                "Eliminar cita",
                                                color = MaterialTheme.colorScheme.onError
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Si aún no se ha seleccionado una fecha
            /*Text(
                "Por favor, selecciona una fecha para visualizar las citas programadas.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 16.dp)
            )*/

            // Mostramos a traves de una consulta las ultimas 10 citas que tenga desde el dia de hoy en
            // orden ascendente

            val historial = dbHelper.obtenerHistorialCitas(limit = 10)

            if (historial.isNotEmpty()) {
                Text("📜 Historial de últimas citas:", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    // Cita de esa fecha, hora, servicio
                    items(historial) { cita ->
                        Text("• ${cita.fecha} - ${cita.hora} - ${cita.servicio}")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }

    // Mostramos un diálogo para confirmar la eliminación de la cita
    citaAEliminar?.let { cita ->
        AlertDialog(
            onDismissRequest = { citaAEliminar = null },
            confirmButton = {
                TextButton(onClick = {
                    // Eliminamos la cita y actualizamos la lista
                    dbHelper.eliminarCita(cita.id)
                    citas = dbHelper.obtenerCitasPorFecha(fechaSeleccionada)
                    Toast.makeText(context, "Cita eliminada correctamente.", Toast.LENGTH_SHORT)
                        .show()
                    citaAEliminar = null
                }) {
                    Text("Sí, eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { citaAEliminar = null }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás segura de que deseas eliminar esta cita? Esta acción no se puede deshacer.") }
        )
    }

    // Diálogo para elegir un cliente antes de crear la cita
    if (mostrarDialogoClientes) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoClientes = false },
            confirmButton = {},
            title = { Text("Selecciona un cliente para agendar la cita") },
            text = {
                Column {
                    clientes.forEach { cliente ->
                        Text(
                            text = cliente.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    mostrarDialogoClientes = false
                                    navController.navigate(NuevaCitaScreenRoute(clienteId = cliente.id))
                                }
                                .padding(vertical = 10.dp)
                        )
                    }
                }
            }
        )
    }
}
