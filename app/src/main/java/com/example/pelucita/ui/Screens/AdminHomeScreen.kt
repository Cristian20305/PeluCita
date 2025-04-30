import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pelucita.Data.Model.Cita
import com.example.pelucita.Data.Repository.DBHelper

@Composable
fun AdminHomeScreen() {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }
    var citas by remember { mutableStateOf(emptyList<Cita>()) }
    var searchQuery by remember { mutableStateOf("") }

    // Cargamos todas las citas al iniciar
    LaunchedEffect(Unit) {
        citas = dbHelper.obtenerTodasLasCitas()
    }

    // Filtramos las citas en función del buscador
    val citasFiltradas = citas.filter { cita ->
        val query = searchQuery.lowercase()
        cita.fecha.lowercase().contains(query) ||
                cita.hora.lowercase().contains(query) ||
                cita.servicio.lowercase().contains(query) ||
                (cita.peluquero?.lowercase()?.contains(query) ?: false) ||
                cita.clienteId.toString().contains(query)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Historial de Citas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buscador SearchView
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista filtrada reciclerView por columnas de lado 
        LazyColumn {
            items(citasFiltradas) { cita ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Fecha: ${cita.fecha}")
                        Text("Hora: ${cita.hora}")
                        Text("Servicio: ${cita.servicio}")
                        Text("Peluquero: ${cita.peluquero ?: "No asignado"}")
                        Text("ID Cliente: ${cita.clienteId}")
                    }
                }
            }
        }
    }
}
