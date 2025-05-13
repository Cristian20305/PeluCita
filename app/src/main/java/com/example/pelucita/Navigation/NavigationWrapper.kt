package com.example.pelucita.Navigation

import AdminHomeScreen
import ClienteHomeScreen
import NuevaCitaScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pelucita.ui.screens.*

@Composable
fun NavigationWrapper(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginScreenRoute) {

        // Pantalla de login
        composable<LoginScreenRoute> {
            LoginScreen(
                onLoginSuccess = { usuarioId, esAdmin ->
                    if (esAdmin) {
                        navController.navigate(AdminHomeScreenRoute)
                    } else {
                        navController.navigate(ClienteHomeScreenRoute(usuarioId))
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
                onRegistroExitoso = { usuarioId ->
                    navController.navigate(ClienteHomeScreenRoute(usuarioId)) {
                        // Opcional: limpiar backstack para que no vuelva al login
                        popUpTo(LoginScreenRoute) { inclusive = true }
                    }
                }
            )
        }

        // Home del cliente
        composable<ClienteHomeScreenRoute> { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getInt("clienteId") ?: 0
            ClienteHomeScreen(
                clienteId = clienteId,
                onVerCita = { citaId ->
                    navController.navigate(CitaDetalleRoute(citaId))
                },
                onNuevaCita = {
                    navController.navigate(NuevaCitaScreenRoute(clienteId))
                }
            )
        }

        // Nueva cita
        composable<NuevaCitaScreenRoute> { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getInt("clienteId") ?: 0
            NuevaCitaScreen(
                clienteId = clienteId,
                onCitaGuardada = {
                    navController.popBackStack()
                }
            )
        }

        // Detalle de una cita
        composable<CitaDetalleRoute> { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            CitaDetalleScreen(citaId = citaId, navController)
        }

        // Home del admin
        composable<AdminHomeScreenRoute> {
            AdminHomeScreen(navController)
        }
    }
}
