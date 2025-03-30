package com.example.pelucita.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pelucita.R


@Composable
fun LoginScreen(onLoginSucces: (esAdmin: Boolean) -> Unit, onRegistrarse: () -> Unit) {

    //Variables de email y contraseña
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Texto para iniciar sesion pantalla de inicio principal
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        //Campo de texto para poner el correo
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        //Campo de texto para poner la contraseña
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

        Button(onClick = {
            //Aqui ponemos la logica real de la validacion que vamos a utilizar para nuestro registro del ADMIN o PROPIETARIO
            // por ejemplo como utilizamos el correo que tenga la palabra admin
            val esAdmin = email.contains("admin") //Por ejemplo
            onLoginSucces(esAdmin)
        }) {
            Text("Entrar")
        }

        //Para los usuarios que no tienen cuenta que se puedan registrar nos lleva a otra PANTALLA
        TextButton(onClick = onRegistrarse) {
            Text("¿No tienes cuenta? Regístrate")
        }

    }


}