package com.example.creadorform.interprete.analizadorsemantico

import com.example.creadorform.interprete.Visitor
import com.example.creadorform.interprete.ast.*

/*
    Analizador semántico que se encarga de verificar que el programa tenga sentido, que las variables estén declaradas antes de usarse y que los tipos sean compatibles.
 */
class AnalizadorSemantico : Visitor<Tipo> {

    val errores = mutableListOf<String>()

    private var tablaDeSimbolos: TablaDeSimbolos = TablaDeSimbolos()

    fun analizarSemantica(ast : AST){
        println("Semantica analizada correctamente")
    }

    override fun visit(nodo: NodoPrograma?): Tipo {
        val sentencias = nodo?.sentencias ?: emptyList()
        val elementos = nodo?.elementos ?: emptyList()
        for (sentencia in sentencias) {
            sentencia.aceptar(this)
        }
        for (elemento in elementos) {
            elemento.aceptar(this)
        }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoBloque?): Tipo {
        val sentencias = nodo?.sentencias ?: emptyList()
        for (sentencia in sentencias) {
            sentencia.aceptar(this)
        }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoListaElementos?): Tipo {
        val elementos = nodo?.elementos ?: emptyList()
        for (elemento in elementos) {
            elemento.aceptar(this)
        }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoListaExpresiones?): Tipo {
        val expresiones = nodo?.expresiones ?: emptyList()

        for (expresion in expresiones) {
            expresion.aceptar(this)
        }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoFilasTabla?): Tipo {
        val filas = nodo?.filas ?: emptyList()
        for (fila in filas) {
            for (celda in fila) {
                celda.aceptar(this)
            }
        }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoEstilos?): Tipo {
        val estilos = nodo?.estilos ?: emptyList()
        for (estilo in estilos) {
            estilo.aceptar(this)
        }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoEstilo?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoAtributo?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoBorde?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoSeccion?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoTexto?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoEmoji?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoTabla?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPregunta?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPreguntaAbierta?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPreguntaSeleccionUnica?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPreguntaSeleccionMultiple?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoPreguntaDesplegable?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoDeclaracionVariable?): Tipo {
        val declaracionCorrecta = tablaDeSimbolos.declarar(nodo?.identificador ?: "", Tipo.valueOf(nodo?.tipo ?: "ERROR"))

        if (!declaracionCorrecta) {
            errores.add("Error semántico: variable ya declarada: ${nodo?.identificador?:""}")
        }

        if (nodo != null) {
            if (nodo.expresionInicial != null) {
                val tExpr = nodo.expresionInicial.aceptar(this)

                if (tExpr != Tipo.valueOf(nodo.tipo)) {
                    errores.add("Error semántico: tipo incompatible: ${nodo.identificador}")
                }
            }
        }

        return Tipo.ERROR
    }

    override fun visit(nodo: NodoDeclaracionSpecial?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoAsignacion?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoSentenciaElemento?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoIf?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoElseIf?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoElse?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoFinIf?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoWhile?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoDoWhile?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoForClasico?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoForRango?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoExpresionBinaria?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoExpresionUnaria?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoLiteral?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoIdentificador?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoCadenaCompuesta?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoLlamadaMetodo?): Tipo? {
        TODO("Not yet implemented")
    }

    override fun visit(nodo: NodoComodin?): Tipo? {
        TODO("Not yet implemented")
    }


}