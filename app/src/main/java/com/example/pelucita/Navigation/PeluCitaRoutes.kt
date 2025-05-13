package com.example.pelucita.Navigation

import kotlinx.serialization.Serializable

// Ruta para la pantalla de Login
@Serializable
object LoginScreenRoute

// Ruta para la pantalla de Cliente pasando id
@Serializable
data class ClienteHomeScreenRoute(val clienteId: Int)

// Ruta para la pantalla de Admin
@Serializable
object AdminHomeScreenRoute

// Ruta para la pantalla de Registro para un nuevo Cliente
@Serializable
object RegistroScreenRoute

// Ruta para la pantalla de Citas, pasando id
@Serializable
data class CitaDetalleRoute(val citaId: Int)

//Ruta para una nueva cita, pasando el id
@Serializable
data class NuevaCitaScreenRoute(val clienteId: Int)
