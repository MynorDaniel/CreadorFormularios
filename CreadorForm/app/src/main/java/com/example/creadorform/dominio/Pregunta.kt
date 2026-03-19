package com.example.creadorform.dominio

open class Pregunta(
    label: String = "",
    width: Int = 0,
    height: Int = 0,
    estilos: Estilos = Estilos()
) : Elemento(width, height, estilos) {

    private var _label: String = label

    val label: String get() = _label

    fun setLabel(valor: String) {
        _label = valor
    }
}

