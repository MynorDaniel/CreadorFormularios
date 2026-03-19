package com.example.creadorform.dominio

open class Elemento(
    width: Int = 0,
    height: Int = 0,
    estilos: Estilos = Estilos()
) {

    private var _width: Int = width
    private var _height: Int = height
    private var _estilos: Estilos = estilos

    val width: Int get() = _width
    val height: Int get() = _height
    val estilos: Estilos get() = _estilos

    fun setWidth(valor: Int) {
        _width = valor
    }

    fun setHeight(valor: Int) {
        _height = valor
    }

    fun setEstilos(valor: Estilos) {
        _estilos = valor
    }
}