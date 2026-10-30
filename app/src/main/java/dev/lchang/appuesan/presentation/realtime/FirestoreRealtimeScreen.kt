package dev.lchang.appuesan.presentation.realtime

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

private val firestoreInstance =
    FirebaseFirestore.getInstance()
        .collection("events")
        .document("task")

@Composable
fun FirestoreRealtimeScreen(){
    var contador by remember { mutableStateOf(0L) }
    var mensaje by remember {mutableStateOf("")}
    var mensajeInput by remember {mutableStateOf("")}
    var conectado by remember {mutableStateOf(false)}
    var errorMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val listener = firestoreInstance.addSnapshotListener { snapshot, error ->
            if (error != null) {
                conectado = false
                errorMessage = error.message
                return@addSnapshotListener
            }
            errorMessage = null
            if (snapshot != null && snapshot.exists()) {
                contador = snapshot.getLong("contador") ?: 0L
                mensaje = snapshot.getString("mensaje") ?: ""
            }
            conectado = true
        }
         onDispose { listener.remove() }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ){
        Text("Firestore en tiempo real", style = MaterialTheme.typography.titleLarge)
        Text(
            if (conectado) "Conectado" else "Conectando...",
            style = MaterialTheme.typography.titleMedium
        )
        errorMessage?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }

        Text("Contador: $contador", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = {
            // Debe ser el mismo campo que lee el listener ("contador")
            firestoreInstance.set(mapOf("contador" to FieldValue.increment(1)), SetOptions.merge())
                .addOnFailureListener { errorMessage = it.message }
        }) {
            Text("+1")
        }
        Text("Mensaje actual: $mensaje")

        OutlinedTextField(
            value = mensajeInput,
            onValueChange = {mensajeInput = it},
            placeholder = {Text("Mensaje")},
            label = {Text("Mensaje")},
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                firestoreInstance.set(mapOf("mensaje" to mensajeInput), SetOptions.merge())
                    .addOnFailureListener { errorMessage = it.message }
            }, modifier = Modifier.fillMaxWidth()
        ){
            Text("Actualizar mensaje")
        }
    }
}