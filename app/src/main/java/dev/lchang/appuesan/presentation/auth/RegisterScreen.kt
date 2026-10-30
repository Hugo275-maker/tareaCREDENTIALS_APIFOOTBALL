package dev.lchang.appuesan.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.lchang.appuesan.data.remote.FirebaseAuthManager
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController)
{
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de usuario", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = name,
            onValueChange = {name = it},
            placeholder = {Text("Nombre Completo")},
            label = {Text("Nombre Completo")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            placeholder = {Text("Correo")},
            label = {Text("Correo")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            placeholder = {Text("Contraseña")},
            label = {Text("Contraseña")},
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            placeholder = {Text("Confirmar Contraseña")},
            label = {Text("Confirmar Contraseña")},
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            enabled = name.isNotBlank() && email.isNotBlank() &&
                    password.isNotBlank() && password == confirmPassword,
            onClick = {
                scope.launch {
                    val result = FirebaseAuthManager.registerUser(name, email, password)
                    if(result.isSuccess)
                        navController.popBackStack()
                    else {
                        //Toast message
                        val error = result.exceptionOrNull()?.message ?:"Error desconocido"
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        )
        {
            Text("Registrar")
        }

    }
}