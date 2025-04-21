package com.example.pelucita.Data.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PeluCitaDatabase(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        // Tabla de usuarios
        db.execSQL(
            """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                contrasena TEXT NOT NULL,
                rol TEXT NOT NULL
            )
        """
        )

        // Tabla de citas
        db.execSQL(
            """
            CREATE TABLE citas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id INTEGER NOT NULL,
                fecha TEXT NOT NULL,
                hora TEXT NOT NULL,
                servicio TEXT NOT NULL,
                peluquero TEXT,
                FOREIGN KEY (cliente_id) REFERENCES usuarios(id)
            )
        """
        )

        // Insertamos un usuario admin predefinido
        db.execSQL(
            """
        INSERT INTO usuarios (nombre, email, contrasena, rol)
        VALUES ('Administrador', 'admin@gmail.com', 'admin123', 'admin')
        """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS citas")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "pelucita.db"
        const val DATABASE_VERSION = 2
    }
}
