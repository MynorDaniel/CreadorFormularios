package com.example.creadorform.dominio

class PreguntaUnica(
    label: String = "",
    opciones: MutableList<String> = mutableListOf(),
    opcionCorrecta: Int = -1,
    width: Int = 0,
    height: Int = 0,
    estilos: Estilos = Estilos()
) : Pregunta(label, width, height, estilos) {

    private val _opciones: MutableList<String> = opciones
    private var _opcionCorrecta: Int = opcionCorrecta

    val opciones: List<String> get() = _opciones
    val opcionCorrecta: Int get() = _opcionCorrecta

    fun addOpcion(valor: String) {
        _opciones.add(valor)
    }

    fun removeOpcion(valor: String): Boolean {
        return _opciones.remove(valor)
    }

    fun clearOpciones() {
        _opciones.clear()
    }

    fun setOpciones(nuevas: MutableList<String>) {
        _opciones.clear()
        _opciones.addAll(nuevas)
    }

    fun setOpcionCorrecta(valor: Int) {
        _opcionCorrecta = valor
    }
}

