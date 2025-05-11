import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.pelucita.Data.Model.Cita
import com.example.pelucita.Data.Model.Peluquero
import com.example.pelucita.Data.Model.Servicio
import com.example.pelucita.Data.Repository.DBHelper
import com.example.pelucita.Utils.CitaReminderWorker
import com.example.pelucita.Utils.HoraDropdown
import com.example.pelucita.Utils.añadirCitaAlCalendario
import com.example.pelucita.Utils.generarHorasDisponibles
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaCitaScreen(
    clienteId: Int,
    onCitaGuardada: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }

    var fecha by remember { mutableStateOf("") }
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

    var mostrarDialogoCalendario by remember { mutableStateOf(false) }
    var citaRecienGuardada by remember { mutableStateOf<Cita?>(null) }


    // Cargamos servicios y peluqueros al inicio
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
            val ocupadas = dbHelper.obtenerCitasPorFecha(selectedDate).map { it.hora }
            horasLibres = generarHorasDisponibles().filterNot { it in ocupadas }
            horaSeleccionada = "" // Reset
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
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
                if (fecha.isEmpty()) "📅 Seleccionar fecha" else "📅 Fecha: $fecha",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (horasLibres.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            // Divimos las horas en dos grupos mañanas y tardes
            val (manana, tarde) = horasLibres.partition { it < "15:00" }
            val horasConEtiqueta = manana.map { "🌅 $it" } + tarde.map { "🌇 $it" }

            // El desplegable para mostrar la hora y eligarla
            HoraDropdown(
                label = "Selecciona una hora disponible",
                horas = horasConEtiqueta,
                horaSeleccionada = horaSeleccionada,
                onHoraSeleccionada = { horaSeleccionada = it.takeLast(5) }
            )

        } else if (fecha.isNotEmpty()) {
            Text(
                "⚠️ No hay horas disponibles",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Dropdown para servicio
        ExposedDropdownMenuBox(
            expanded = expandedServicio,
            onExpandedChange = { expandedServicio = !expandedServicio },
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
                    .menuAnchor()
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

        // Dropdown para peluquero (opcional)
        ExposedDropdownMenuBox(
            expanded = expandedPeluquero,
            onExpandedChange = { expandedPeluquero = !expandedPeluquero },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Campo de peluquero (opcional)
            OutlinedTextField(
                value = peluquero,
                onValueChange = { },
                label = { Text("💇 Peluquero (opcional)") },
                placeholder = { Text("Ej: María, Juan...") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPeluquero) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
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
                    val nuevaCita = Cita(
                        id = 0,
                        clienteId = clienteId,
                        fecha = fecha,
                        hora = horaSeleccionada,
                        servicio = servicio,
                        peluquero = if (peluquero.isBlank()) null else peluquero
                    )
                    val citaId = dbHelper.crearCita(nuevaCita)

                    if (citaId != -1L) {
                        val citaGuardada = dbHelper.obtenerCitaPorId(citaId.toInt())
                        citaGuardada?.let {
                            // Convertir fecha + hora a Date
                            val citaDateTime =
                                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                    .parse("${it.fecha} ${it.hora}")

                            citaDateTime?.let { date ->
                                // Aqui funciona normal te avisa una hora antes
                                //val reminderTime = date.time - 60 * 60 * 1000 // 1h antes
                                //val delay = reminderTime - System.currentTimeMillis()
                                // Prueba de 30 segundos para ver si funciona
                                val delay = 30 * 1000L

                                if (delay > 0) {
                                    val workRequest =
                                        OneTimeWorkRequestBuilder<CitaReminderWorker>()
                                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                            .setInputData(
                                                workDataOf(
                                                    "citaId" to it.id,
                                                    "fecha" to it.fecha,
                                                    "hora" to it.hora
                                                )
                                            )
                                            .build()
                                    WorkManager.getInstance(context).enqueue(workRequest)
                                }
                            }

                            Toast.makeText(
                                context,
                                "✅ Cita guardada correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            citaRecienGuardada = it
                            mostrarDialogoCalendario = true
                        }
                    } else {
                        Toast.makeText(context, "❌ Error al guardar la cita", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "⚠️ Por favor completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar cita")
        }

        // Dialogo para preguntar si quiere añadir la cita al calendario
        if (mostrarDialogoCalendario && citaRecienGuardada != null) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoCalendario = false },
                title = { Text("Añadir al calendario") },
                text = { Text("¿Quieres añadir esta cita al calendario de tu móvil?") },
                confirmButton = {
                    TextButton(onClick = {
                        añadirCitaAlCalendario(context, citaRecienGuardada!!)
                        mostrarDialogoCalendario = false
                        onCitaGuardada()
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        mostrarDialogoCalendario = false
                        onCitaGuardada()
                    }) {
                        Text("No")
                    }
                }
            )
        }

    }
}
