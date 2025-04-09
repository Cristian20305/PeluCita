package com.example.pelucita.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ClienteHomeScreen(onVerCita: (Int) -> Unit) {


    val citas = listOf(1, 2, 3)

    Column {
        Text("Bienvenido/a, Cliente")
        citas.forEach { id ->
            Button(onClick = { onVerCita(id) }) {
                Text("Ver Cita $id")
            }
        }
    }

}