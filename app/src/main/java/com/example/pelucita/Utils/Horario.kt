package com.example.pelucita.Utils
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

fun generarHorasDisponibles(): List<String> {
    val horasDisponibles = mutableListOf<String>()

    // Turno de mañana: 10:00 - 14:00
    var hora = 10
    var minuto = 0
    while (hora < 14 || (hora == 14 && minuto == 0)) {
        horasDisponibles.add("%02d:%02d".format(hora, minuto))
        minuto += 30
        if (minuto >= 60) {
            minuto = 0
            hora += 1
        }
    }

    // Turno de tarde: 16:30 - 20:00
    hora = 16
    minuto = 30
    while (hora < 20 || (hora == 20 && minuto == 0)) {
        horasDisponibles.add("%02d:%02d".format(hora, minuto))
        minuto += 30
        if (minuto >= 60) {
            minuto = 0
            hora += 1
        }
    }

    return horasDisponibles
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraDropdown(
    label: String,
    horas: List<String>,
    horaSeleccionada: String,
    onHoraSeleccionada: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        OutlinedTextField(
            value = horaSeleccionada,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            horas.forEach { hora ->
                DropdownMenuItem(
                    text = { Text(hora) },
                    onClick = {
                        onHoraSeleccionada(hora)
                        expanded = false
                    }
                )
            }
        }
    }
}
