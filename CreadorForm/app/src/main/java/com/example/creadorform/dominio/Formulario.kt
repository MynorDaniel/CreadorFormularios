package com.example.creadorform.dominio

class Formulario {

    private val _elementos = mutableListOf<Elemento>()

    val elementos: List<Elemento> get() = _elementos

    fun addElemento(elemento: Elemento) {
        _elementos.add(elemento)
    }

    fun removeElemento(elemento: Elemento): Boolean {
        return _elementos.remove(elemento)
    }

    fun clearElementos() {
        _elementos.clear()
    }

    fun setElementos(nuevos: MutableList<Elemento>) {
        _elementos.clear()
        _elementos.addAll(nuevos)
    }

    override fun toString(): String {
        if (_elementos.isEmpty()) return "Formulario(vacio)"

        val sb = StringBuilder()
        sb.appendLine("Formulario")
        _elementos.forEachIndexed { i, e ->
            val isLast = i == _elementos.lastIndex
            sb.append(e.toTreeString("", isLast))
        }
        return sb.toString().trimEnd()
    }

}

internal fun Elemento.toTreeString(prefijo: String, esUltimo: Boolean): String {
    val rama = if (esUltimo) "└── " else "├── "
    val nuevoPrefijo = prefijo + if (esUltimo) "    " else "│   "
    val sb = StringBuilder()
    sb.append(prefijo).append(rama).append(this.resumen()).append('\n')

    when (this) {
        is Seccion -> {
            elementos.forEachIndexed { idx, hijo ->
                sb.append(hijo.toTreeString(nuevoPrefijo, idx == elementos.lastIndex))
            }
        }
        is Tabla -> {
            lineas.forEachIndexed { li, linea ->
                sb.append(linea.toTreeString(nuevoPrefijo, li == lineas.lastIndex))
            }
        }
        is Linea -> {
            elementos.forEachIndexed { idx, hijo ->
                sb.append(hijo.toTreeString(nuevoPrefijo, idx == elementos.lastIndex))
            }
        }
    }

    return sb.toString()
}

internal fun Elemento.resumen(): String {
    fun estilosStr(): String = estilos.toString()
    return when (this) {
        is Texto -> "Texto(width=$width, height=$height, contenido=${contenido.quote()}, estilos=${estilosStr()})"
        is Seccion -> "Seccion(width=$width, height=$height, label=${label.quote()}, orientation=$orientacion, x=$pointX, y=$pointY, estilos=${estilosStr()})"
        is Tabla -> "Tabla(width=$width, height=$height, estilos=${estilosStr()})"
        is Linea -> "Linea(width=$width, height=$height, estilos=${estilosStr()})"
        is PreguntaDesplegable -> "PreguntaDesplegable(width=$width, height=$height, label=${label.quote()}, opciones=${opciones.joinToString(prefix = "[", postfix = "]") { it.quote() }}, correcta=$opcionCorrecta, estilos=${estilosStr()})"
        is PreguntaUnica -> "PreguntaUnica(width=$width, height=$height, label=${label.quote()}, opciones=${opciones.joinToString(prefix = "[", postfix = "]") { it.quote() }}, correcta=$opcionCorrecta, estilos=${estilosStr()})"
        is PreguntaMultiple -> "PreguntaMultiple(width=$width, height=$height, label=${label.quote()}, opciones=${opciones.joinToString(prefix = "[", postfix = "]") { it.quote() }}, correctas=${opcionesCorrectas.joinToString(prefix = "[", postfix = "]")}, estilos=${estilosStr()})"
        is PreguntaAbierta -> "PreguntaAbierta(width=$width, height=$height, label=${label.quote()}, estilos=${estilosStr()})"
        is Pregunta -> "Pregunta(width=$width, height=$height, label=${label.quote()}, estilos=${estilosStr()})"
        else -> "Elemento(${this::class.simpleName}, width=$width, height=$height, estilos=${estilosStr()})"
    }
}

private fun String.quote(): String = buildString {
    append('"')
    append(this@quote.replace("\n", "\\n"))
    append('"')
}
