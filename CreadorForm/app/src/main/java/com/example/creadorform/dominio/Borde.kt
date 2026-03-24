package com.example.creadorform.dominio

class Borde(
    grosor: Int = 0,
    tipo: TipoBorde = TipoBorde.LINE,
    color: Color = Color()
) {

    private var _grosor: Int = grosor
    private var _tipo: TipoBorde = tipo
    private var _color: Color = color

    val grosor: Int get() = _grosor
    val tipo: TipoBorde get() = _tipo
    val color: Color get() = _color

    fun setGrosor(valor: Int) {
        _grosor = valor
    }

    fun setTipo(valor: TipoBorde) {
        _tipo = valor
    }

    fun setColor(valor: Color) {
        _color = valor
    }

    override fun toString(): String {
        return "Borde(grosor=$grosor, tipo=$tipo, color=$color)"
    }
}

