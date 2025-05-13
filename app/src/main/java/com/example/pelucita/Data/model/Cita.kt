package com.example.pelucita.Data.model

data class Cita(
    val id: Int = 0,
    val clienteId: Int,
    val fecha: String,
    val hora: String,
    val servicio: String,
    val peluquero: String?
)