package com.example.pelucita.Data.Repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.pelucita.Data.Database.PeluCitaDatabase
import com.example.pelucita.Data.Model.Cita
import com.example.pelucita.Data.Model.Peluquero
import com.example.pelucita.Data.Model.Servicio
import com.example.pelucita.Data.Model.Usuario

class DBHelper(context: Context) {
    private val db = PeluCitaDatabase(context).writableDatabase

    // Insertar usuario
    fun registrarUsuario(nombre: String, email: String, contrasena: String, rol: String): Long {
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("email", email)
            put("contrasena", contrasena)
            put("rol", rol)
        }
        return db.insert("usuarios", null, valores)
    }

    // Validar login
    fun login(email: String, contrasena: String): Usuario? {
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE email = ? AND contrasena = ?",
            arrayOf(email, contrasena)
        )
        val user = if (cursor.moveToFirst()) {
            Usuario(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                contrasena = cursor.getString(cursor.getColumnIndexOrThrow("contrasena")),
                rol = cursor.getString(cursor.getColumnIndexOrThrow("rol"))
            )
        } else null
        cursor.close()
        return user
    }

    // Insertar cita
    fun crearCita(cita: Cita): Long {
        val valores = ContentValues().apply {
            put("cliente_id", cita.clienteId)
            put("fecha", cita.fecha)
            put("hora", cita.hora)
            put("servicio", cita.servicio)
            put("peluquero", cita.peluquero)
        }
        return db.insert("citas", null, valores)
    }

    // Obtener citas por cliente
    fun obtenerCitasPorCliente(clienteId: Int): List<Cita> {
        val lista = mutableListOf<Cita>()
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM citas WHERE cliente_id = ?", arrayOf(clienteId.toString())
        )
        while (cursor.moveToNext()) {
            lista.add(
                Cita(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    clienteId = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id")),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                    servicio = cursor.getString(cursor.getColumnIndexOrThrow("servicio")),
                    peluquero = cursor.getString(cursor.getColumnIndexOrThrow("peluquero"))
                )
            )
        }
        cursor.close()
        return lista
    }

    // Obtener detalles de una cita
    fun obtenerCitaPorId(id: Int): Cita? {
        val cursor = db.rawQuery("SELECT * FROM citas WHERE id = ?", arrayOf(id.toString()))
        val cita = if (cursor.moveToFirst()) {
            Cita(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                clienteId = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id")),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                servicio = cursor.getString(cursor.getColumnIndexOrThrow("servicio")),
                peluquero = cursor.getString(cursor.getColumnIndexOrThrow("peluquero"))
            )
        } else null
        cursor.close()
        return cita
    }

    // Mostramos todas las citas
    fun obtenerTodasLasCitas(): List<Cita> {

        val lista = mutableListOf<Cita>()
        val cursor = db.rawQuery("SELECT * FROM citas", null)
        while (cursor.moveToNext()) {
            lista.add(
                Cita(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    clienteId = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id")),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                    servicio = cursor.getString(cursor.getColumnIndexOrThrow("servicio")),
                    peluquero = cursor.getString(cursor.getColumnIndexOrThrow("peluquero"))
                )
            )
        }
        cursor.close()
        return lista
    }

    // Obtenemos citas por esas horas que pasamis
    fun obtenerCitasPorFecha(fecha: String): List<Cita> {
        val lista = mutableListOf<Cita>()
        val cursor = db.rawQuery(
            "SELECT * FROM citas WHERE fecha = ?",
            arrayOf(fecha)
        )
        while (cursor.moveToNext()) {
            lista.add(
                Cita(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    clienteId = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id")),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                    servicio = cursor.getString(cursor.getColumnIndexOrThrow("servicio")),
                    peluquero = cursor.getString(cursor.getColumnIndexOrThrow("peluquero"))
                )
            )
        }
        cursor.close()
        return lista
    }

    // Eliminamos una cita de la base de datos
    fun eliminarCita(citaId: Int): Int {
        return db.delete("citas", "id = ?", arrayOf(citaId.toString()))
    }

    // Podemis actualizar o editar una cita
    fun actualizarCita(cita: Cita): Int {
        val valores = ContentValues().apply {
            put("fecha", cita.fecha)
            put("hora", cita.hora)
            put("servicio", cita.servicio)
            put("peluquero", cita.peluquero)
        }
        return db.update("citas", valores, "id = ?", arrayOf(cita.id.toString()))
    }

    fun obtenerTodosLosUsuarios(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE rol = 'cliente'", null)

        while (cursor.moveToNext()) {
            val usuario = Usuario(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                contrasena = cursor.getString(cursor.getColumnIndexOrThrow("contrasena")),
                rol = cursor.getString(cursor.getColumnIndexOrThrow("rol"))
            )
            lista.add(usuario)
        }
        cursor.close()
        return lista
    }

    fun obtenerTodosLosPeluqueros(): List<Peluquero> {
        val peluqueros = mutableListOf<Peluquero>()
        val cursor = db.rawQuery("SELECT id, nombre FROM peluqueros", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val nombre = cursor.getString(1)
            peluqueros.add(Peluquero(id, nombre))
        }
        cursor.close()
        return peluqueros
    }

    fun obtenerTodosLosServicios(): List<Servicio> {
        val servicios = mutableListOf<Servicio>()
        val cursor = db.rawQuery("SELECT id, nombre FROM servicios", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val nombre = cursor.getString(1)
            servicios.add(Servicio(id, nombre))
        }
        cursor.close()
        return servicios
    }

    fun obtenerHistorialCitas(limit: Int = 10): List<Cita> {
        val lista = mutableListOf<Cita>()
        val cursor = db.rawQuery(
            "SELECT * FROM citas ORDER BY fecha DESC, hora DESC LIMIT ?",
            arrayOf(limit.toString())
        )
        while (cursor.moveToNext()) {
            lista.add(cursorToCita(cursor))
        }
        cursor.close()
        return lista
    }

    // Convierte un cursor en un objeto Cita
    private fun cursorToCita(cursor: Cursor): Cita {
        return Cita(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            clienteId = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id")),
            fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
            hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
            servicio = cursor.getString(cursor.getColumnIndexOrThrow("servicio")),
            peluquero = cursor.getString(cursor.getColumnIndexOrThrow("peluquero"))
        )
    }


}
