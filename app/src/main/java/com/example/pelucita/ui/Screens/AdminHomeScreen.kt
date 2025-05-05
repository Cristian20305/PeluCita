import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pelucita.Data.Model.Cita
import com.example.pelucita.Data.Model.Usuario
import com.example.pelucita.Data.Repository.DBHelper
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
        Text("Calendario de citas", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para abrir el calendario y seleccionar fecha
        Button(onClick = { datePicker.show() }) {
            // Si ya hay una fecha seleccionada, la mostramos
            Text(if (fechaSeleccionada.isEmpty()) "Seleccionar fecha" else "Fecha: $fechaSeleccionada")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para añadir nueva cita como admin
        Button(
            onClick = {
                // Al pulsar, cargamos los clientes y mostramos el diálogo
                clientes = dbHelper.obtenerTodosLosUsuarios()
                mostrarDialogoClientes = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("➕ Nueva cita (admin)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostramos las citas del día si hay una fecha seleccionada
        if (fechaSeleccionada.isNotEmpty()) {
            if (citas.isEmpty()) {
                // Si no hay citas, mensaje informativo
                Text("No hay citas para esta fecha", color = MaterialTheme.colorScheme.outline)
            } else {
                // Lista de citas en ese día
                LazyColumn {
                    items(citas) { cita ->
                        // Tarjeta con los datos de la cita
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Hora: ${cita.hora}")
                                Text("Servicio: ${cita.servicio}")
                                Text("Peluquero: ${cita.peluquero ?: "No asignado"}")
                                Text("ID Cliente: ${cita.clienteId}")

                                Spacer(modifier = Modifier.height(8.dp))

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
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar")
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Botón para eliminar con confirmación
                                    Button(
                                        onClick = { citaAEliminar = cita },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Eliminar", color = MaterialTheme.colorScheme.onError)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Si aún no se ha seleccionado una fecha
            Text("Selecciona una fecha para ver las citas")
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
                    Toast.makeText(context, "Cita eliminada", Toast.LENGTH_SHORT).show()
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
            title = { Text("¿Eliminar esta cita?") },
            text = { Text("¿Estás segura de que quieres eliminar esta cita? Esta acción no se puede deshacer.") }
        )
    }

    // Diálogo para elegir un cliente antes de crear la cita
    if (mostrarDialogoClientes) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoClientes = false },
            confirmButton = {},
            title = { Text("Selecciona un cliente") },
            text = {
                Column {
                    clientes.forEach { cliente ->
                        Text(
                            text = "${cliente.nombre} (ID: ${cliente.id})",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    mostrarDialogoClientes = false
                                    navController.navigate(NuevaCitaScreenRoute(clienteId = cliente.id))
                                }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        )
    }
}
