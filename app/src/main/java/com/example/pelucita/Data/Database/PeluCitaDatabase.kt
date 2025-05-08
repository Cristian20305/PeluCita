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
        // Tabla de peluqueros
        db.execSQL(
            """CREATE TABLE peluqueros(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            )
        """
        )

        // Tabla de servicios
        db.execSQL(
            """CREATE TABLE servicios(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
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

        // Insertar peluqueros por defecto
        db.execSQL("INSERT INTO peluqueros (nombre) VALUES ('Ana')")
        db.execSQL("INSERT INTO peluqueros (nombre) VALUES ('Carlos')")
        db.execSQL("INSERT INTO peluqueros (nombre) VALUES ('Laura')")

        // Insertar servicios por defecto
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Corte de pelo')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Tinte')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Peinado')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Mechas')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Balayage')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Alisado')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Permanente')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Recogido')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Trenzas')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Extensiones')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Mascarilla capilar')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Tratamiento de keratina')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Desrizado')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Corte infantil')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Corte caballero')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Corte señora')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Peinado de novia')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Coloración fantasía')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Lavado + secado')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Brushing')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Decoloración')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Retoque de raíces')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Corte flequillo')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Peinado con ondas')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Tratamiento anticaída')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Moldeado')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Barbería - Afeitado')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Barbería - Perfilado')")
        db.execSQL("INSERT INTO servicios (nombre) VALUES ('Tratamiento hidratante')")


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS citas")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS peluqueros")
        db.execSQL("DROP TABLE IF EXISTS servicios")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "pelucita.db"
        const val DATABASE_VERSION = 5
    }
}
