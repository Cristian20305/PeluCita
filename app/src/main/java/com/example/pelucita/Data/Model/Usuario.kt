package com.example.pelucita.Data.Model

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val rol: String // "cliente" o "admin"
)