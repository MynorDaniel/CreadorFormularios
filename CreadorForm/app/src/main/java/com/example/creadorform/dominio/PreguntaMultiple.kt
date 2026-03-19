package com.example.creadorform.dominio

class PreguntaMultiple(
    label: String = "",
    opciones: MutableList<String> = mutableListOf(),
    opcionesCorrectas: MutableList<Int> = mutableListOf(),
    width: Int = 0,
    height: Int = 0,
    estilos: Estilos = Estilos()
) : Pregunta(label, width, height, estilos) {

    private val _opciones: MutableList<String> = opciones
    private val _opcionesCorrectas: MutableList<Int> = opcionesCorrectas

    val opciones: List<String> get() = _opciones
    val opcionesCorrectas: List<Int> get() = _opcionesCorrectas

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

    fun addOpcionCorrecta(valor: Int) {
        _opcionesCorrectas.add(valor)
    }

    fun removeOpcionCorrecta(valor: Int): Boolean {
        return _opcionesCorrectas.remove(valor)
    }

    fun clearOpcionesCorrectas() {
        _opcionesCorrectas.clear()
    }

    fun setOpcionesCorrectas(nuevas: MutableList<Int>) {
        _opcionesCorrectas.clear()
        _opcionesCorrectas.addAll(nuevas)
    }
}

