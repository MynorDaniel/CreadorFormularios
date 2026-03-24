package com.example.creadorform

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creadorform.datos.GestorArchivos
import com.example.creadorform.dominio.*
import com.example.creadorform.interprete.Traductor
import java.util.Locale

@Composable
fun FormularioRendererAuto(
    formulario: Formulario,
    modifier: Modifier = Modifier
) {
    var mostrarCorrectasGlobal by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        items(formulario.elementos) { el ->
            ElementoAuto(el, mostrarCorrectasGlobal)
        }

        item {
            Button(
                onClick = { mostrarCorrectasGlobal = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }
        }
    }
}

@Composable
private fun ElementoAuto(elemento: Elemento, mostrarCorrectas: Boolean) {
    when (elemento) {
        is Texto -> TextoAuto(elemento)
        is Seccion -> SeccionAuto(elemento, mostrarCorrectas)
        is Tabla -> TablaAuto(elemento, mostrarCorrectas)
        is Linea -> LineaAuto(elemento, mostrarCorrectas)

        is PreguntaAbierta -> PreguntaAbiertaAuto(elemento)
        is PreguntaUnica -> PreguntaUnicaAuto(elemento, mostrarCorrectas)
        is PreguntaMultiple -> PreguntaMultipleAuto(elemento, mostrarCorrectas)
        is PreguntaDesplegable -> PreguntaDesplegableAuto(elemento, mostrarCorrectas)

        else -> CajaElemento(elemento) {
            Text("Elemento no soportado: ${elemento::class.simpleName}", style = elemento.estilos.textStyle())
        }
    }
}

private fun Fuente.toComposeFontFamily(): FontFamily = when (this) {
    Fuente.MONO -> FontFamily.Monospace
    Fuente.SANS_SERIF -> FontFamily.SansSerif
    Fuente.CURSIVE -> FontFamily.Cursive
}

private fun Color.toComposeColorOrNull(): ComposeColor? {
    val hex = colorHex.trim()
    return try {
        val v = hex.removePrefix("#")
        val argb = when (v.length) {
            6 -> ("FF$v").toLong(16)
            else -> return null
        }
        ComposeColor(argb)
    } catch (_: Throwable) {
        null
    }
}

private fun Estilos.textStyle(): TextStyle {
    val c = color.toComposeColorOrNull() ?: ComposeColor.Unspecified
    val size = if (sizeTexto <= 0) 14 else sizeTexto
    return TextStyle(
        color = c,
        fontFamily = fuente.toComposeFontFamily(),
        fontSize = size.sp
    )
}

private fun Estilos.bg(): ComposeColor =
    colorFondo.toComposeColorOrNull() ?: ComposeColor.Transparent

private fun Estilos.borderOrNull(): BorderStroke? {
    val g = borde.grosor
    if (g <= 0) return null
    val col = borde.color.toComposeColorOrNull() ?: ComposeColor.Black
    return BorderStroke(g.dp, col)
}

@Composable
private fun CajaElemento(
    elemento: Elemento,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val estilos = elemento.estilos
    val shape = RoundedCornerShape(10.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .let { base ->
                val m1 = if (estilos.bg() != ComposeColor.Transparent) base.background(estilos.bg(), shape) else base
                val b = estilos.borderOrNull()
                if (b != null) m1.border(b, shape) else m1
            }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}


@Composable
private fun TextoAuto(t: Texto) {
    CajaElemento(t) {
        Text(t.contenido, style = t.estilos.textStyle())
    }
}

@Composable
private fun SeccionAuto(s: Seccion, mostrarCorrectas: Boolean) {
    CajaElemento(s) {
        if (s.label.isNotBlank()) {
            Text(
                s.label,
                style = s.estilos.textStyle().copy(fontSize = (maxOf(14, s.estilos.sizeTexto) + 2).sp)
            )
        }

        when (s.orientacion) {
            Orientacion.VERTICAL -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    s.elementos.forEach { ElementoAuto(it, mostrarCorrectas) }
                }
            }
            Orientacion.HORIZONTAL -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    s.elementos.forEach { child ->
                        Box(Modifier.weight(1f, fill = true)) {
                            ElementoAuto(child, mostrarCorrectas)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TablaAuto(t: Tabla, mostrarCorrectas: Boolean) {
    CajaElemento(t) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            t.lineas.forEach { linea ->
                LineaAuto(linea, mostrarCorrectas)
            }
        }
    }
}

@Composable
private fun LineaAuto(l: Linea, mostrarCorrectas: Boolean) {
    CajaElemento(l) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            l.elementos.forEach { el ->
                Box(Modifier.weight(1f, fill = true)) {
                    ElementoAuto(el, mostrarCorrectas)
                }
            }
        }
    }
}

@Composable
private fun PreguntaHeader(p: Pregunta, tipo: String) {
    Text(tipo, style = p.estilos.textStyle().copy(fontSize = (maxOf(14, p.estilos.sizeTexto) + 2).sp))
    if (p.label.isNotBlank()) Text(p.label, style = p.estilos.textStyle())
}

@Composable
private fun PreguntaAbiertaAuto(p: PreguntaAbierta) {
    var respuesta by remember { mutableStateOf("") }
    var editable by remember { mutableStateOf(true) }

    CajaElemento(p) {
        PreguntaHeader(p, "Pregunta abierta")

        OutlinedTextField(
            value = respuesta,
            onValueChange = { respuesta = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 200.dp),
            readOnly = !editable,
            label = { Text("Respuesta") }
        )

    }
}

@Composable
private fun PreguntaUnicaAuto(p: PreguntaUnica, mostrarCorrecta: Boolean) {
    var seleccion by remember { mutableStateOf<Int?>(null) }

    CajaElemento(p) {
        PreguntaHeader(p, "Selección única")

        p.opciones.forEachIndexed { idx, opt ->
            val esCorrecta = idx == p.opcionCorrecta
            val sufijo = if (mostrarCorrecta && esCorrecta) " (correcta)" else ""

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = seleccion == idx,
                    onClick = { seleccion = idx }
                )
                Text(opt + sufijo)
            }
        }

        OutlinedButton(onClick = { seleccion = null }) { Text("Limpiar") }
    }
}

@Composable
private fun PreguntaMultipleAuto(p: PreguntaMultiple, mostrarCorrectas: Boolean) {
    val checks = remember(p.opciones.size) {
        mutableStateListOf<Boolean>().apply { repeat(p.opciones.size) { add(false) } }
    }

    CajaElemento(p) {
        PreguntaHeader(p, "Selección múltiple")

        p.opciones.forEachIndexed { idx, opt ->
            val esCorrecta = p.opcionesCorrectas.contains(idx)
            val sufijo = if (mostrarCorrectas && esCorrecta) " (correcta)" else ""

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checks.getOrNull(idx) == true,
                    onCheckedChange = { v -> if (idx in checks.indices) checks[idx] = v }
                )
                Text(opt + sufijo)
            }
        }

        OutlinedButton(onClick = {
            for (i in checks.indices) checks[i] = false
        }) { Text("Limpiar") }
    }
}

@Composable
private fun PreguntaDesplegableAuto(p: PreguntaDesplegable, mostrarCorrecta: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    var seleccion by remember { mutableStateOf<Int?>(null) }

    val opciones = p.opciones
    val seleccionadoText = seleccion?.let { opciones.getOrNull(it) } ?: "Selecciona una opción"
    val correctaText = opciones.getOrNull(p.opcionCorrecta)

    CajaElemento(p) {
        PreguntaHeader(p, "Desplegable")

        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) { Text(seleccionadoText) }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEachIndexed { idx, opt ->
                    DropdownMenuItem(
                        text = { Text(opt) },
                        onClick = {
                            seleccion = idx
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedButton(onClick = { seleccion = null }) { Text("Limpiar") }

        if (mostrarCorrecta) {
            Text(
                text = "Correcta: ${correctaText ?: "(no definida)"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun normalizarNombreBase(nombreArchivoBase: String): String {
    val limpio = nombreArchivoBase.trim().ifBlank { "formulario" }
    val lower = limpio.lowercase(Locale.ROOT)
    return when {
        lower.endsWith(".form") -> limpio.dropLast(5)
        lower.endsWith(".pkm") -> limpio.dropLast(4)
        else -> limpio
    }.trim().ifBlank { "formulario" }
}

private fun resultadoGuardadoEsValido(valor: Any?): Boolean =
    valor !is Boolean || valor

data class ResultadoGuardadoDual(
    val base: String,
    val guardoForm: Boolean,
    val guardoPkm: Boolean
) {
    val exitoTotal: Boolean get() = guardoForm && guardoPkm
}

fun guardarTextoEditorEnDosFormatos(
    gestorArchivos: GestorArchivos,
    nombreArchivoBase: String,
    textoEditor: String,
    formulario: Formulario? = null,
    carpetaDestinoUri: Uri? = null
): ResultadoGuardadoDual {
    val base = normalizarNombreBase(nombreArchivoBase)

    val guardoForm = runCatching {
        gestorArchivos.guardarForm("$base.form", textoEditor, carpetaDestinoUri)
    }.map(::resultadoGuardadoEsValido).getOrDefault(false)

    val contenidoPkm = formulario?.let { Traductor().traducirAPKM(it) }

    val guardoPkm = if (contenidoPkm == null) {
        false
    } else {
        runCatching {
            gestorArchivos.guardarPKM("$base.pkm", contenidoPkm, carpetaDestinoUri)
        }.map(::resultadoGuardadoEsValido).getOrDefault(false)
    }

    return ResultadoGuardadoDual(base, guardoForm, guardoPkm)
}


@Composable
fun BotonGuardarEditor(
    gestorArchivos: GestorArchivos,
    nombreArchivoBase: String,
    textoEditor: String,
    formulario: Formulario? = null,
    carpetaDestinoUri: Uri?,
    modifier: Modifier = Modifier
) {
    var mensaje by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {
                val resultado = guardarTextoEditorEnDosFormatos(
                    gestorArchivos = gestorArchivos,
                    nombreArchivoBase = nombreArchivoBase,
                    textoEditor = textoEditor,
                    formulario = formulario,
                    carpetaDestinoUri = carpetaDestinoUri
                )
                mensaje = when {
                    resultado.exitoTotal -> "Guardado: ${resultado.base}.form y ${resultado.base}.pkm"
                    formulario == null && resultado.guardoForm -> "Se guardó ${resultado.base}.form. No se generó ${resultado.base}.pkm: crea el formulario primero."
                    !resultado.guardoForm && !resultado.guardoPkm -> "No se pudo guardar ${resultado.base}.form ni ${resultado.base}.pkm"
                    !resultado.guardoForm -> "Se guardó ${resultado.base}.pkm, pero falló ${resultado.base}.form"
                    else -> "Se guardó ${resultado.base}.form, pero falló ${resultado.base}.pkm"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar") }

        mensaje?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
    }
}



