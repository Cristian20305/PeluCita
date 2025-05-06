package com.example.pelucita

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.pelucita.Navigation.NavigationWrapper
import com.example.pelucita.ui.theme.PeluCitaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {  // Cambiamos a AppCompatActivity

    private var keepSplashVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition { keepSplashVisible }

        super.onCreate(savedInstanceState)

        // Ocultar barra de arriba
        supportActionBar?.hide()

        // Temporizador para alargar el splash (3.5 segundos)
        lifecycleScope.launch {
            delay(3500)
            keepSplashVisible = false
        }

        setContent {
            PeluCitaTheme {
                NavigationWrapper()
            }
        }
    }
}
