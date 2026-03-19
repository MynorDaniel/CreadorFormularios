package com.example.creadorform.dominio

class Texto(
    contenido: String = "",
    width: Int = 0,
    height: Int = 0,
    estilos: Estilos = Estilos()
) : Elemento(width, height, estilos) {

    private var _contenido: String = contenido

    val contenido: String get() = _contenido

    fun setContenido(valor: String) {
        _contenido = valor
    }
}

