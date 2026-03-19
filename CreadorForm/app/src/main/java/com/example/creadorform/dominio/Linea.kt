package com.example.creadorform.dominio

class Linea(
    elementos: MutableList<Elemento> = mutableListOf(),
    width: Int = 0,
    height: Int = 0,
    estilos: Estilos = Estilos()
) : Elemento(width, height, estilos) {

    private val _elementos: MutableList<Elemento> = elementos

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
}

