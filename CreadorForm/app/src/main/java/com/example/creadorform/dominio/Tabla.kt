package com.example.creadorform.dominio

class Tabla(
    lineas: MutableList<Linea> = mutableListOf(),
    width: Int = 0,
    height: Int = 0,
    estilos: Estilos = Estilos()
) : Elemento(width, height, estilos) {

    private val _lineas: MutableList<Linea> = lineas

    val lineas: List<Linea> get() = _lineas

    fun addLinea(linea: Linea) {
        _lineas.add(linea)
    }

    fun removeLinea(linea: Linea): Boolean {
        return _lineas.remove(linea)
    }

    fun clearLineas() {
        _lineas.clear()
    }

    fun setLineas(nuevas: MutableList<Linea>) {
        _lineas.clear()
        _lineas.addAll(nuevas)
    }
}

