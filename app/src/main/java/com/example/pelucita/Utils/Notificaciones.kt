package com.example.pelucita.Utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.pelucita.R

class CitaReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val citaId = inputData.getInt("citaId", -1)
        val fecha = inputData.getString("fecha") ?: ""
        val hora = inputData.getString("hora") ?: ""

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "cita_recordatorio",
                "Recordatorios de Citas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones para recordarte tus citas en PeluCita"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "cita_recordatorio")
            .setContentTitle("¡Recordatorio de tu cita! 💇")
            .setContentText("Tu cita es hoy a las $hora. ¡No llegues tarde!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)  // <-- Aquí tenemis un icono que saldra en nuestra notificacion
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        Log.d("CitaReminderWorker", "Mostrando notificación para citaId=$citaId a las $hora")

        notificationManager.notify(citaId, notification)

        return Result.success()
    }

}
