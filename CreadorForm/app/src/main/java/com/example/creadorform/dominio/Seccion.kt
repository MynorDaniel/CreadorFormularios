package com.example.creadorform.dominio

class Seccion(
    label: String = "",
    pointX: Int = 0,
    pointY: Int = 0,
    orientacion: Orientacion = Orientacion.VERTICAL,
    elementos: MutableList<Elemento> = mutableListOf(),
    width: Int = 0,
    height: Int = 0,
    estilos: Estilos = Estilos()
) : Elemento(width, height, estilos) {

    private var _label: String = label
    private var _pointX: Int = pointX
    private var _pointY: Int = pointY
    private var _orientacion: Orientacion = orientacion
    private val _elementos: MutableList<Elemento> = elementos

    val label: String get() = _label
    val pointX: Int get() = _pointX
    val pointY: Int get() = _pointY
    val orientacion: Orientacion get() = _orientacion
    val elementos: List<Elemento> get() = _elementos

    fun setLabel(valor: String) {
        _label = valor
    }

    fun setPointX(valor: Int) {
        _pointX = valor
    }

    fun setPointY(valor: Int) {
        _pointY = valor
    }

    fun setOrientacion(valor: Orientacion) {
        _orientacion = valor
    }

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
}

