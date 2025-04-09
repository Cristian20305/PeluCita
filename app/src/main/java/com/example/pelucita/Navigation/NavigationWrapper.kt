package com.example.pelucita.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pelucita.ui.Screens.AdminHomeScreen
import com.example.pelucita.ui.Screens.CitaDetalleScreen
import com.example.pelucita.ui.Screens.ClienteHomeScreen
import com.example.pelucita.ui.Screens.LoginScreen
import com.example.pelucita.ui.Screens.RegistroScreen

@Composable
fun NavigationWrapper(modifier: Modifier) {

    // Controlador de navegación
    val navController = rememberNavController()

    // Definimos las rutas de navegación
    NavHost(navController = navController, startDestination = LoginScreenRoute) {

        // Pantalla de login
        composable<LoginScreenRoute> {
            LoginScreen(
                onLoginSucces = { esAdmin ->
                    if (esAdmin) {
                        navController.navigate(AdminHomeScreenRoute)
                    } else {
                        navController.navigate(ClienteHomeScreenRoute)
                    }
                },
                onRegistrarse = {
                    navController.navigate(RegistroScreenRoute)
                }
            )
        }

        // Pantalla de registro
        composable<RegistroScreenRoute> {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate(ClienteHomeScreenRoute)
                }
            )
        }

        // Pantalla principal del cliente
        composable<ClienteHomeScreenRoute> {
            ClienteHomeScreen(onVerCita = { citaId ->
                navController.navigate(CitaDetalleRoute(citaId))
            })
        }

        // Pantalla principal del administrador
        composable<AdminHomeScreenRoute> {
            AdminHomeScreen()
        }

        // Pantalla de detalle de una cita
        composable<CitaDetalleRoute> { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            CitaDetalleScreen(citaId = citaId)
        }
    }
}
