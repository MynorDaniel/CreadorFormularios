package com.example.creadorform.interprete.analizadorsemantico

class TablaDeSimbolos{

    private val tabla = mutableMapOf<String, Tipo>()

    fun declarar(id: String, tipo: Tipo): Boolean {
        if (tabla.containsKey(id)) return false
        tabla[id] = tipo
        return true
    }

    fun buscar(id: String): Tipo? {
        return tabla[id]
    }
}