package com.example.creadorform.interprete

import com.example.creadorform.dominio.Elemento
import com.example.creadorform.dominio.Estilos
import com.example.creadorform.dominio.Formulario
import com.example.creadorform.dominio.Linea
import com.example.creadorform.dominio.PreguntaAbierta
import com.example.creadorform.dominio.PreguntaDesplegable
import com.example.creadorform.dominio.PreguntaMultiple
import com.example.creadorform.dominio.PreguntaUnica
import com.example.creadorform.dominio.Seccion
import com.example.creadorform.dominio.Tabla
import com.example.creadorform.dominio.Texto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Traductor {

    fun traducirAPKM(formulario: Formulario): String {
        val stats = Estadisticas()
        formulario.elementos.forEach { acumularStats(it, stats) }

        val now = Date()
        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(now)
        val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)

        val sb = StringBuilder()
        sb.appendLine("###")
        sb.appendLine("Fecha: $fecha")
        sb.appendLine("Hora: $hora")
        sb.appendLine("Total de Secciones: ${stats.totalSecciones}")
        sb.appendLine("Total de Preguntas: ${stats.totalPreguntas}")
        sb.appendLine("    Abiertas: ${stats.abiertas}")
        sb.appendLine("    Desplegables: ${stats.desplegables}")
        sb.appendLine("    Seleccion: ${stats.seleccionUnica}")
        sb.appendLine("    Multiples: ${stats.multiples}")
        sb.appendLine("###")
        sb.appendLine()

        formulario.elementos.forEach { elemento ->
            sb.append(traducirElemento(elemento, 0))
            if (!sb.endsWith("\n\n")) {
                sb.appendLine()
            }
        }

        return sb.toString().trimEnd()
    }

    private fun acumularStats(elemento: Elemento, stats: Estadisticas) {
        when (elemento) {
            is Seccion -> {
                stats.totalSecciones++
                elemento.elementos.forEach { acumularStats(it, stats) }
            }

            is Tabla -> {
                elemento.lineas.forEach { linea ->
                    linea.elementos.forEach { acumularStats(it, stats) }
                }
            }

            is PreguntaAbierta -> {
                stats.totalPreguntas++
                stats.abiertas++
            }

            is PreguntaDesplegable -> {
                stats.totalPreguntas++
                stats.desplegables++
            }

            is PreguntaUnica -> {
                stats.totalPreguntas++
                stats.seleccionUnica++
            }

            is PreguntaMultiple -> {
                stats.totalPreguntas++
                stats.multiples++
            }
        }
    }

    private fun traducirElemento(elemento: Elemento, nivel: Int): String {
        return when (elemento) {
            is Seccion -> traducirSeccion(elemento, nivel)
            is Tabla -> traducirTabla(elemento, nivel)
            is Linea -> traducirLinea(elemento, nivel)
            is Texto -> traducirTexto(elemento, nivel)
            is PreguntaAbierta -> traducirPreguntaAbierta(elemento, nivel)
            is PreguntaDesplegable -> traducirPreguntaDesplegable(elemento, nivel)
            is PreguntaUnica -> traducirPreguntaUnica(elemento, nivel)
            is PreguntaMultiple -> traducirPreguntaMultiple(elemento, nivel)
            else -> "${ind(nivel)}# Elemento no soportado: ${elemento::class.simpleName}\n"
        }
    }

    private fun traducirSeccion(seccion: Seccion, nivel: Int): String {
        val i = ind(nivel)
        val sb = StringBuilder()
        sb.appendLine("$i<section=${seccion.width},${seccion.height},${seccion.pointX},${seccion.pointY},${seccion.orientacion}>")
        sb.append(traducirEstilos(seccion.estilos, nivel + 1))
        sb.appendLine("${ind(nivel + 1)}<content>")
        seccion.elementos.forEach { hijo -> sb.append(traducirElemento(hijo, nivel + 2)) }
        sb.appendLine("${ind(nivel + 1)}</content>")
        sb.appendLine("$i</section>")
        return sb.toString()
    }

    private fun traducirTabla(tabla: Tabla, nivel: Int): String {
        val i = ind(nivel)
        val sb = StringBuilder()
        sb.appendLine("$i<table=${tabla.width},${tabla.height},0,0>")
        sb.append(traducirEstilos(tabla.estilos, nivel + 1))
        sb.appendLine("${ind(nivel + 1)}<content>")
        tabla.lineas.forEach { linea -> sb.append(traducirLinea(linea, nivel + 2)) }
        sb.appendLine("${ind(nivel + 1)}</content>")
        sb.appendLine("$i</table>")
        return sb.toString()
    }

    private fun traducirLinea(linea: Linea, nivel: Int): String {
        val i = ind(nivel)
        val sb = StringBuilder()
        sb.appendLine("$i<line>")
        linea.elementos.forEach { elemento ->
            sb.appendLine("${ind(nivel + 1)}<element>")
            sb.append(traducirElemento(elemento, nivel + 2))
            sb.appendLine("${ind(nivel + 1)}</element>")
        }
        sb.appendLine("$i</line>")
        return sb.toString()
    }

    private fun traducirTexto(texto: Texto, nivel: Int): String {
        val i = ind(nivel)
        val sb = StringBuilder()
        sb.appendLine("$i<text=${texto.width},${texto.height},${q(texto.contenido)}>")
        sb.append(traducirEstilos(texto.estilos, nivel + 1))
        sb.appendLine("$i</text>")
        return sb.toString()
    }

    private fun traducirPreguntaAbierta(p: PreguntaAbierta, nivel: Int): String {
        val i = ind(nivel)
        val sb = StringBuilder()
        sb.appendLine("$i<open=${p.width},${p.height},${q(p.label)}>")
        sb.append(traducirEstilos(p.estilos, nivel + 1))
        sb.appendLine("$i</open>")
        return sb.toString()
    }

    private fun traducirPreguntaDesplegable(p: PreguntaDesplegable, nivel: Int): String {
        val i = ind(nivel)
        val opciones = p.opciones.joinToString(prefix = "{", postfix = "}") { q(it) }
        val correcta = if (p.opcionCorrecta >= 0) p.opcionCorrecta else -1
        val sb = StringBuilder()
        sb.appendLine("$i<drop=${p.width},${p.height},${q(p.label)},$opciones,$correcta>")
        sb.append(traducirEstilos(p.estilos, nivel + 1))
        sb.appendLine("$i</drop>")
        return sb.toString()
    }

    private fun traducirPreguntaUnica(p: PreguntaUnica, nivel: Int): String {
        val i = ind(nivel)
        val opciones = p.opciones.joinToString(prefix = "{", postfix = "}") { q(it) }
        val correcta = if (p.opcionCorrecta >= 0) p.opcionCorrecta else 0
        val sb = StringBuilder()
        sb.appendLine("$i<select=${p.width},${p.height},${q(p.label)},$opciones,$correcta>")
        sb.append(traducirEstilos(p.estilos, nivel + 1))
        sb.appendLine("$i</select>")
        return sb.toString()
    }

    private fun traducirPreguntaMultiple(p: PreguntaMultiple, nivel: Int): String {
        val i = ind(nivel)
        val opciones = p.opciones.joinToString(prefix = "{", postfix = "}") { q(it) }
        val correctas = p.opcionesCorrectas.joinToString(prefix = "{", postfix = "}")
        val sb = StringBuilder()
        sb.appendLine("$i<multiple=${p.width},${p.height},${q(p.label)},$opciones,$correctas>")
        sb.append(traducirEstilos(p.estilos, nivel + 1))
        sb.appendLine("$i</multiple>")
        return sb.toString()
    }

    private fun traducirEstilos(estilos: Estilos, nivel: Int): String {
        val i = ind(nivel)
        val b = estilos.borde
        return buildString {
            appendLine("$i<style>")
            appendLine("${ind(nivel + 1)}<color=${estilos.color}/>")
            appendLine("${ind(nivel + 1)}<background color=${estilos.colorFondo}/>")
            appendLine("${ind(nivel + 1)}<font family=${estilos.fuente}/>")
            appendLine("${ind(nivel + 1)}<text size=${estilos.sizeTexto}/>")
            appendLine("${ind(nivel + 1)}<border=${b.grosor},${b.tipo},color=${b.color}/>")
            appendLine("$i</style>")
        }
    }

    private fun ind(nivel: Int): String = "    ".repeat(nivel)

    private fun q(valor: String): String {
        val conEmojisDsl = normalizarEmojisATexto(valor)
        val limpio = conEmojisDsl
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
        return if (limpio.isEmpty()) "\"-\"" else "\"$limpio\""
    }

    private fun normalizarEmojisATexto(valor: String): String {
        return valor
            .replace("❤️", "@[:heart:]")
            .replace("❤", "@[:heart:]")
            .replace("😀", "@[:smile:]")
            .replace("😢", "@[:sad:]")
            .replace("🥲", "@[:sad:]")
            .replace("😐", "@[:serious:]")
            .replace("⭐", "@[:star:]")
            .replace("🐱", "@[:cat:]")
            .replace("😺", "@[:cat:]")
    }

    private data class Estadisticas(
        var totalSecciones: Int = 0,
        var totalPreguntas: Int = 0,
        var abiertas: Int = 0,
        var desplegables: Int = 0,
        var seleccionUnica: Int = 0,
        var multiples: Int = 0
    )
}