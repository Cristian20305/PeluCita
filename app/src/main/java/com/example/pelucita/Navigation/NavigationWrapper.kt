package com.example.pelucita.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pelucita.ui.Screens.LoginScreen
import com.example.pelucita.ui.Screens.RegistroScreen

@Composable
fun NavigationWrapper(modifier: Modifier) {

    // Objeto que controla la navegación
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginScreenRoute) {

        composable<LoginScreenRoute> {
            LoginScreen(
                // Dependiendo de si es ADMIN o no accedemos a una pantalla o otra
                // Si es admin, vamos al home de admin
                // Si no, vamos al home de cliente
                onLoginSucces = { esAdmin ->
                    if (esAdmin) {
                        navController.navigate(AdminHomeScreenRoute)
                    } else {
                        navController.navigate(ClienteHomeScreenRoute)
                    }
                },
                // Si no es ninguna de esas cuentas, vamos a nuestra pantalla de registro
                onRegistrarse = {
                    navController.navigate(RegistroScreenRoute)
                }
            )
        }

        // Pantalla de registro
        composable<RegistroScreenRoute> {
            RegistroScreen(onRegistroExitoso = {
                // Si se completa existoxamente lo llevamos al home del cliente
                navController.navigate(ClienteHomeScreenRoute)
            })
        }

        /**
        composable<RegistroScreenRoute> {
        ClienteHomeScreen(onVerCita = { citaId ->
        navController.navigate(CitaDetalleRoute(citaId))
        })
        }

        composable<AdminHomeScreenRoute> {
        AdminHomeScreenn()
        }

        composable<CitaDetalleRoute> { backStackEntry ->
        val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
        CitaDetalleScreen(citaId = citaId)
        }
         **/

    }

}