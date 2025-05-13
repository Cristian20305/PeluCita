package com.example.pelucita.Utils

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.example.pelucita.Data.model.Cita
import java.text.SimpleDateFormat
import java.util.*


// Funciones de calendarios aqui las tendremos para mejor organizacion


// Añadimos al calendario a traves de un intent la cita como recordatorio
fun añadirCitaAlCalendario(context: Context, cita: Cita) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val fechaHora = sdf.parse("${cita.fecha} ${cita.hora}") ?: return

    val inicio = Calendar.getInstance().apply {
        time = fechaHora
    }
    val fin = Calendar.getInstance().apply {
        time = fechaHora
        add(Calendar.MINUTE, 30) // Duración de 30 min
    }

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, inicio.timeInMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, fin.timeInMillis)
        putExtra(CalendarContract.Events.TITLE, "Cita en PeluCita - ${cita.servicio}")
        putExtra(CalendarContract.Events.EVENT_LOCATION, "Tu peluquería")
        putExtra(
            CalendarContract.Events.DESCRIPTION,
            "Servicio: ${cita.servicio} - Peluquero: ${cita.peluquero ?: "No asignado"}"
        )
    }

    // Lanza el intent
    context.startActivity(intent)
}
