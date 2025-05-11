package com.example.pelucita.ui.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pelucita.Data.Repository.DBHelper
import com.example.pelucita.R


@Composable
fun LoginScreen(
    onLoginSuccess: (usuarioId: Int, esAdmin: Boolean) -> Unit,
    onRegistrarse: () -> Unit
) {
    // Variables
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Texto de inicio de sesión
        Text(
            text = "🔐 Iniciar Sesión",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para el email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // Campo de texto para la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            // Procedemos a cambiar la contraseña para que se pueda ver al pulsar en un icono de un ojoo
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                //Controlo con imagenes que me he descargado que se pueda mostrar el texto de la contraseña o no y tambien
                // que el icono se cambie o n o dependiendo de si se muestra o no el texto
                val imageEyes = if (passwordVisible) R.drawable.eye_off else R.drawable.eye
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Image(
                        painter = painterResource(id = imageEyes),
                        contentDescription = "Mostrar/Ocultar contraseña",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Botón de entrar
        Button(onClick = {
            // Validar el usuario en la base de datos
            val usuario = dbHelper.login(email.trim(), password)
            if (usuario != null) {
                // Si existe, devolvemos su ID y si es admin o no
                val esAdmin = usuario.rol == "admin"
                onLoginSuccess(usuario.id, esAdmin)
            } else {
                Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de registrarse
        TextButton(onClick = onRegistrarse) {
            Text("¿No tienes cuenta? Regístrate")
        }

    }


}