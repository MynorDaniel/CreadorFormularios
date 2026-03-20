package com.example.creadorform.interprete

import com.example.creadorform.dominio.*
import com.example.creadorform.interprete.ast.*

class Evaluador : Visitor<Any> {

    fun evaluar(nodo: NodoPrograma): Formulario {
        return nodo.aceptar(this) as Formulario
    }

    override fun visit(nodo: NodoPrograma?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoBloque?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoListaElementos?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoListaExpresiones?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoFilasTabla?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoEstilos?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoEstilo?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoAtributo?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoBorde?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoSeccion?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoTexto?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoEmoji?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoTabla?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPregunta?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPreguntaAbierta?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPreguntaSeleccionUnica?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPreguntaSeleccionMultiple?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPreguntaDesplegable?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoDeclaracionVariable?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoDeclaracionSpecial?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoAsignacion?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoSentenciaElemento?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoIf?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoElseIf?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoElse?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoFinIf?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoWhile?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoDoWhile?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoForClasico?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoForRango?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoExpresionBinaria?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoExpresionUnaria?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoLiteral?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoIdentificador?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoCadenaCompuesta?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoLlamadaMetodo?): Any? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoComodin?): Any? {
        TODO("Not yet implemented")
    }


}