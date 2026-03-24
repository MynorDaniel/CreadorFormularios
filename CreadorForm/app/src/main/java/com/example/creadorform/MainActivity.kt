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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.creadorform.controlador.AnalisisControlador
import com.example.creadorform.datos.GestorArchivos
import com.example.creadorform.datos.GestorPlantillas
import com.example.creadorform.dominio.Formulario
import com.example.creadorform.ui.theme.CreadorFormTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Context
import android.provider.OpenableColumns
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


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
    var fuente by remember { mutableStateOf(TextFieldValue("")) }

    var cargando by remember { mutableStateOf(false) }
    var errores by remember { mutableStateOf("") }
    var consola by remember { mutableStateOf("") }
    var contenidoPkmImportado by remember { mutableStateOf("") }

    val context = LocalContext.current
    val gestorArchivos = remember(context) { GestorArchivos(context) }
    val gestorPlantillas = remember { GestorPlantillas() }
    var nombreArchivoBase by remember { mutableStateOf("formulario") }
    var carpetaDestinoUri by remember { mutableStateOf<Uri?>(null) }

    var menuAbierto by remember { mutableStateOf(false) }

    val importarFormLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        scope.launch {
            val nombre = context.obtenerNombreArchivo(uri).orEmpty()

            if (!nombre.endsWith(".form", ignoreCase = true)) {
                consola = buildString {
                    appendLine("Consola")
                    appendLine("Archivo inválido: selecciona un .form")
                    if (nombre.isNotBlank()) appendLine("Seleccionado: $nombre")
                }
                return@launch
            }

            val texto = context.leerTextoDesdeUri(uri)
            if (texto.isBlank()) {
                consola = buildString {
                    appendLine("Consola")
                    appendLine("No se pudo leer el archivo .form o está vacío.")
                    appendLine("Archivo: $nombre")
                }
                return@launch
            }

            fuente = TextFieldValue(texto, TextRange(texto.length))
            tabSeleccionada = 0
            consola = buildString {
                appendLine("Consola")
                appendLine("Importado correctamente: $nombre")
                appendLine("Se cargó el contenido en el editor.")
            }
        }
    }

    val importarPkmLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        scope.launch {
            val nombre = context.obtenerNombreArchivo(uri).orEmpty()

            if (!nombre.endsWith(".pkm", ignoreCase = true)) {
                consola = buildString {
                    appendLine("Consola")
                    appendLine("Archivo inválido: selecciona un .pkm")
                    if (nombre.isNotBlank()) appendLine("Seleccionado: $nombre")
                }
                return@launch
            }

            val texto = context.leerTextoDesdeUri(uri)
            if (texto.isBlank()) {
                consola = buildString {
                    appendLine("No se pudo leer el archivo .pkm o está vacío.")
                    appendLine("Archivo: $nombre")
                }
                return@launch
            }

            contenidoPkmImportado = texto


            consola = ""
            errores = ""
            formulario = null
            cargando = true

            val analisis = AnalisisControlador()

            val form = withContext(Dispatchers.IO) {
                analisis.analizar(contenidoPkmImportado, false)
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

            println("PKM importado:\n$contenidoPkmImportado")
        }
    }


    val selectorCarpeta = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            runCatching {
                context.contentResolver.takePersistableUriPermission(uri, flags)
            }
            carpetaDestinoUri = uri
        }
    }



    Column(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { menuAbierto = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Menú"
                )
            }

            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Importar form") },
                    onClick = {
                        menuAbierto = false
                        importarFormLauncher.launch(arrayOf("*/*"))
                    }
                )
                DropdownMenuItem(
                    text = { Text("Importar pkm") },
                    onClick = {
                        menuAbierto = false
                        importarPkmLauncher.launch(arrayOf("*/*"))
                    }
                )
            }
        }


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
                formulario = formulario,
                consola = consola,
                cargando = cargando,
                gestorArchivos = gestorArchivos,
                gestorPlantillas = gestorPlantillas,
                nombreArchivoBase = nombreArchivoBase,
                onNombreArchivoBaseChange = { nombreArchivoBase = it },
                carpetaDestinoUri = carpetaDestinoUri,
                onSeleccionarCarpeta = { selectorCarpeta.launch(carpetaDestinoUri) },
                onCrearFormulario = {
                    scope.launch {
                        consola = ""
                        errores = ""
                        formulario = null
                        cargando = true

                        val analisis = AnalisisControlador()

                        val form = withContext(Dispatchers.IO) {
                            analisis.analizar(fuente.text, true)
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
    fuente: TextFieldValue,
    onFuenteChange: (TextFieldValue) -> Unit,
    formulario: Formulario?,
    consola: String,
    cargando: Boolean,
    gestorArchivos: GestorArchivos,
    gestorPlantillas: GestorPlantillas,
    nombreArchivoBase: String,
    onNombreArchivoBaseChange: (String) -> Unit,
    carpetaDestinoUri: Uri?,
    onSeleccionarCarpeta: () -> Unit,
    onCrearFormulario: () -> Unit,
    modifier: Modifier = Modifier
)
{
    var emojiMenuAbierto by remember { mutableStateOf(false) }
    var colorMenuAbierto by remember { mutableStateOf(false) }
    var plantillaMenuAbierto by remember { mutableStateOf(false) }

    val plantillas = remember(gestorPlantillas) {
        listOf(
            "Insertar sección" to gestorPlantillas.obtenerPlantillaSeccion(),
            "Insertar tabla" to gestorPlantillas.obtenerPlantillaTabla(),
            "Insertar texto" to gestorPlantillas.obtenerPlantillaTexto(),
            "Insertar pregunta abierta" to gestorPlantillas.obtenerPlantillaPreguntaAbierta(),
            "Insertar pregunta desplegable" to gestorPlantillas.obtenerPlantillaPreguntaDesplegable(),
            "Insertar pregunta selección única" to gestorPlantillas.obtenerPlantillaPreguntaSeleccionUnica(),
            "Insertar pregunta selección múltiple" to gestorPlantillas.obtenerPlantillaPreguntaSeleccionMultiple(),
            "Insertar IF/ELSE" to gestorPlantillas.obtenerPlantillaIfElse(),
            "Insertar WHILE" to gestorPlantillas.obtenerPlantillaWhile(),
            "Insertar DO-WHILE" to gestorPlantillas.obtenerPlantillaDoWhile(),
            "Insertar FOR clásico" to gestorPlantillas.obtenerPlantillaForClasico(),
            "Insertar FOR rango" to gestorPlantillas.obtenerPlantillaForRango()
        )
    }

    Column(modifier = modifier) {
        Text("Editor", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = nombreArchivoBase,
            onValueChange = onNombreArchivoBaseChange,
            label = { Text("Nombre del archivo (sin extensión)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { emojiMenuAbierto = true }) {
                Text("Insertar emoji")
            }
            DropdownMenu(
                expanded = emojiMenuAbierto,
                onDismissRequest = { emojiMenuAbierto = false }
            ) {
                EMOJIS_EDITOR.forEach { (nombre, literal) ->
                    DropdownMenuItem(
                        text = { Text("$nombre  $literal") },
                        onClick = {
                            onFuenteChange(insertarEnCursor(fuente, literal))
                            emojiMenuAbierto = false
                        }
                    )
                }
            }

            Button(onClick = { colorMenuAbierto = true }) {
                Text("Insertar color")
            }
            DropdownMenu(
                expanded = colorMenuAbierto,
                onDismissRequest = { colorMenuAbierto = false }
            ) {
                COLORES_HEX_EDITOR.forEach { (nombre, hex) ->
                    DropdownMenuItem(
                        text = { Text("$nombre  $hex") },
                        onClick = {
                            onFuenteChange(insertarEnCursor(fuente, hex))
                            colorMenuAbierto = false
                        }
                    )
                }
            }

            Button(onClick = { plantillaMenuAbierto = true }) {
                Text("Insertar plantilla")
            }
            DropdownMenu(
                expanded = plantillaMenuAbierto,
                onDismissRequest = { plantillaMenuAbierto = false }
            ) {
                plantillas.forEach { (nombre, plantilla) ->
                    DropdownMenuItem(
                        text = { Text(nombre) },
                        onClick = {
                            onFuenteChange(insertarEnCursor(fuente, "\n$plantilla\n"))
                            plantillaMenuAbierto = false
                        }
                    )
                }
            }
        }

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onSeleccionarCarpeta) {
                Text("Seleccionar carpeta")
            }
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = carpetaDestinoUri?.toString() ?: "Carpeta destino: almacenamiento interno de la app",
            style = MaterialTheme.typography.bodySmall
        )


        BotonGuardarEditor(
            gestorArchivos = gestorArchivos,
            nombreArchivoBase = nombreArchivoBase,
            textoEditor = fuente.text,
            formulario = formulario,
            carpetaDestinoUri = carpetaDestinoUri
        )


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

private fun Context.obtenerNombreArchivo(uri: Uri): String? {
    contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
        val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (idx >= 0 && c.moveToFirst()) return c.getString(idx)
    }
    return null
}

private suspend fun Context.leerTextoDesdeUri(uri: Uri): String = withContext(Dispatchers.IO) {
    contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use { it.readText() } ?: ""
}

private val EMOJIS_EDITOR = listOf(
    "Risa" to "@[:)]",
    "Triste" to "@[:(]",
    "Serio" to "@[:|]",
    "Corazon" to "@[:heart:]",
    "Estrella" to "@[:star:]",
    "Gato" to "@[:cat:]"
)

private val COLORES_HEX_EDITOR = listOf(
    "Rojo" to "#FF0000",
    "Azul" to "#0000FF",
    "Verde" to "#00FF00",
    "Morado" to "#800080",
    "Celeste" to "#87CEEB",
    "Negro" to "#000000",
    "Blanco" to "#FFFFFF"
)

private fun insertarEnCursor(actual: TextFieldValue, insercion: String): TextFieldValue {
    val inicio = actual.selection.start.coerceIn(0, actual.text.length)
    val fin = actual.selection.end.coerceIn(0, actual.text.length)
    val textoNuevo = actual.text.substring(0, inicio) + insercion + actual.text.substring(fin)
    val cursorNuevo = inicio + insercion.length
    return TextFieldValue(text = textoNuevo, selection = TextRange(cursorNuevo))
}

