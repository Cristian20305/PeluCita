import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pelucita.Data.Model.Cita
import com.example.pelucita.Data.Repository.DBHelper


@Composable
fun ClienteHomeScreen(
    clienteId: Int,
    onVerCita: (Int) -> Unit,
    onNuevaCita: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }
    var citas by remember { mutableStateOf(emptyList<Cita>()) }

    // Cargar citas del cliente
    LaunchedEffect(clienteId) {
        citas = dbHelper.obtenerCitasPorCliente(clienteId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Mis Citas", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(citas) { cita ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clickable { onVerCita(cita.id) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("📅 ${cita.fecha}", style = MaterialTheme.typography.bodyLarge)
                            Text("⏰ ${cita.hora}", style = MaterialTheme.typography.bodyMedium)
                            Text("💇 ${cita.servicio}", style = MaterialTheme.typography.bodyMedium)
                            cita.peluquero?.let {
                                Text("✂️ $it", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { onVerCita(cita.id) }) {
                                Text("Editar")
                            }

                            TextButton(onClick = {
                                dbHelper.eliminarCita(cita.id)
                                citas = dbHelper.obtenerCitasPorCliente(clienteId) // recargar
                            }) {
                                Text("Cancelar", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        // Botón de "+"/agendar cita
        FloatingActionButton(
            onClick = onNuevaCita,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}
