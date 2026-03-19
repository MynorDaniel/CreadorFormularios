package com.example.creadorform.interprete;

import com.example.creadorform.interprete.ast.*;

/**
 * Visitor genérico para recorrer el AST.
 * @param <T> Tipo de retorno de cada visita.
 */
public interface Visitor<T> {
    T visit(NodoPrograma nodo);
    T visit(NodoBloque nodo);
    T visit(NodoListaElementos nodo);
    T visit(NodoListaExpresiones nodo);
    T visit(NodoFilasTabla nodo);
    T visit(NodoEstilos nodo);

    T visit(NodoEstilo nodo);
    T visit(NodoAtributo nodo);
    T visit(NodoBorde nodo);

    T visit(NodoSeccion nodo);
    T visit(NodoTexto nodo);
    T visit(NodoEmoji nodo);
    T visit(NodoTabla nodo);

    T visit(NodoPregunta nodo);
    T visit(NodoPreguntaAbierta nodo);
    T visit(NodoPreguntaSeleccionUnica nodo);
    T visit(NodoPreguntaSeleccionMultiple nodo);
    T visit(NodoPreguntaDesplegable nodo);

    T visit(NodoDeclaracionVariable nodo);
    T visit(NodoDeclaracionSpecial nodo);
    T visit(NodoAsignacion nodo);
    T visit(NodoSentenciaElemento nodo);

    T visit(NodoIf nodo);
    T visit(NodoElseIf nodo);
    T visit(NodoElse nodo);
    T visit(NodoFinIf nodo);

    T visit(NodoWhile nodo);
    T visit(NodoDoWhile nodo);
    T visit(NodoForClasico nodo);
    T visit(NodoForRango nodo);

    T visit(NodoExpresionBinaria nodo);
    T visit(NodoExpresionUnaria nodo);
    T visit(NodoLiteral nodo);
    T visit(NodoIdentificador nodo);
    T visit(NodoCadenaCompuesta nodo);
    T visit(NodoLlamadaMetodo nodo);
    T visit(NodoComodin nodo);
}

