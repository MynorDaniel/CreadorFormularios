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
        return "Formulario vacio"
    }

}