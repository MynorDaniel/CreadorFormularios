package com.example.creadorform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.creadorform.controlador.AnalisisControlador
import com.example.creadorform.dominio.Formulario
import com.example.creadorform.ui.theme.CreadorFormTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreadorFormTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    var formulario by remember { mutableStateOf<Formulario?>(null) }

    var tabSeleccionada by remember { mutableIntStateOf(0) }
    var fuente by remember { mutableStateOf("") }

    var cargando by remember { mutableStateOf(false) }
    var errores by remember { mutableStateOf("") }
    var consola by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {

        TabRow(selectedTabIndex = tabSeleccionada) {
            Tab(
                selected = tabSeleccionada == 0,
                onClick = { tabSeleccionada = 0 },
                text = { Text("Editor") }
            )
            Tab(
                selected = tabSeleccionada == 1,
                onClick = { tabSeleccionada = 1 },
                text = { Text("Formulario") }
            )
        }

        when (tabSeleccionada) {
            0 -> EditorScreen(
                fuente = fuente,
                onFuenteChange = { fuente = it },
                consola = consola,
                cargando = cargando,
                onCrearFormulario = {
                    scope.launch {
                        consola = ""
                        errores = ""
                        formulario = null
                        cargando = true

                        val analisis = AnalisisControlador()

                        val form = withContext(Dispatchers.IO) {
                            analisis.analizarForm(fuente)
                        }

                        errores = analisis.errores
                        formulario = form

                        consola = buildString {
                            appendLine("Consola")
                            if (errores.isNotBlank()) {
                                appendLine("Errores:")
                                appendLine(errores)
                            } else {
                                appendLine("Sin errores.")
                            }
                            appendLine()
                            appendLine("Formulario generado")
                            appendLine(formulario.toString())
                        }

                        cargando = false
                        yield()
                        tabSeleccionada = 1

                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )

            1 -> FormularioScreen(
                formulario = formulario,
                errores = errores,
                cargando = cargando,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun EditorScreen(
    fuente: String,
    onFuenteChange: (String) -> Unit,
    consola: String,
    cargando: Boolean,
    onCrearFormulario: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        Text("Editor", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = fuente,
            onValueChange = onFuenteChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            label = { Text("Código") },
            placeholder = { Text("Escribe tu formulario aquí...") },
            singleLine = false
        )

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = onCrearFormulario,
                enabled = !cargando
            ) {
                Text(if (cargando) "Creando..." else "Crear formulario")
            }
        }

        Spacer(Modifier.height(8.dp))

        Text("Consola / Logs", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = consola,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 200.dp),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            label = { Text("Salida") }
        )
    }
}

@Composable
fun FormularioScreen(
    formulario: Formulario?,
    errores: String,
    cargando: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Formulario", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        when {
            cargando -> {
                Text("Generando formulario...")
            }

            formulario == null -> {
                Text("Aún no has generado un formulario.")
                if (errores.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text("== Errores ==")
                    Text(errores)
                }
            }

            else -> {
                FormularioRendererAuto(formulario = formulario)

            }
        }
    }
}
