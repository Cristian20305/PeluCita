package com.example.pelucita.Utils

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

