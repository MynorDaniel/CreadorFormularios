package com.example.creadorform.dominio

class Estilos(
    color: Color = Color("#000000"),
    colorFondo: Color = Color("#FFFFFF"),
    fuente: Fuente = Fuente.MONO,
    sizeTexto: Int = 14,
    borde: Borde = Borde()
) {

    private var _color: Color = color
    private var _colorFondo: Color = colorFondo
    private var _fuente: Fuente = fuente
    private var _sizeTexto: Int = sizeTexto
    private var _borde: Borde = borde

    val color: Color get() = _color
    val colorFondo: Color get() = _colorFondo
    val fuente: Fuente get() = _fuente
    val sizeTexto: Int get() = _sizeTexto
    val borde: Borde get() = _borde

    fun setColor(valor: Color) {
        _color = valor
    }

    fun setColorFondo(valor: Color) {
        _colorFondo = valor
    }

    fun setFuente(valor: Fuente) {
        _fuente = valor
    }

    fun setSizeTexto(valor: Int) {
        _sizeTexto = valor
    }

    fun setBorde(valor: Borde) {
        _borde = valor
    }
}

