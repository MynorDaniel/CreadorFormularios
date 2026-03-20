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
        errores.clear()
        tablaDeSimbolos = TablaDeSimbolos()

        ast.raiz?.aceptar(this)

        if (errores.isEmpty()) {
            println("Semantica analizada correctamente")
        }
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

    override fun visit(nodo: NodoEstilo?): Tipo {
        nodo?.valor?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoAtributo?): Tipo {
        nodo?.valor?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoBorde?): Tipo {
        nodo?.num?.aceptar(this)
        nodo?.tipo?.aceptar(this)
        nodo?.color?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoSeccion?): Tipo {
        val atributos = nodo?.atributos
        atributos?.forEach { (it as? NodoAtributo)?.aceptar(this) }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoTexto?): Tipo {
        val atributos = nodo?.atributos
        atributos?.forEach { (it as? NodoAtributo)?.aceptar(this) }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoEmoji?): Tipo {
        return Tipo.VOID
    }

    override fun visit(nodo: NodoTabla?): Tipo {
        val atributos = nodo?.atributos
        atributos?.forEach { (it as? NodoAtributo)?.aceptar(this) }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoPregunta?): Tipo {
        return Tipo.SPECIAL
    }

    override fun visit(nodo: NodoPreguntaAbierta?): Tipo {
        val atributos = nodo?.atributos
        atributos?.forEach { (it as? NodoAtributo)?.aceptar(this) }
        return Tipo.SPECIAL
    }

    override fun visit(nodo: NodoPreguntaSeleccionUnica?): Tipo {
        val atributos = nodo?.atributos
        atributos?.forEach { (it as? NodoAtributo)?.aceptar(this) }
        return Tipo.SPECIAL
    }

    override fun visit(nodo: NodoPreguntaSeleccionMultiple?): Tipo {
        val atributos = nodo?.atributos
        atributos?.forEach { (it as? NodoAtributo)?.aceptar(this) }
        return Tipo.SPECIAL
    }

    override fun visit(nodo: NodoPreguntaDesplegable?): Tipo {
        val atributos = nodo?.atributos
        atributos?.forEach { (it as? NodoAtributo)?.aceptar(this) }
        return Tipo.SPECIAL
    }

    override fun visit(nodo: NodoDeclaracionVariable?): Tipo {
        if (nodo == null) return Tipo.ERROR

        val tipoDecl = try {
            Tipo.valueOf(nodo.tipo.uppercase())
        } catch (_: IllegalArgumentException) {
            Tipo.ERROR
        }

        val id = nodo.identificador
        val declaracionCorrecta = tablaDeSimbolos.declarar(id, tipoDecl)
        if (!declaracionCorrecta) {
            errores.add("Error semántico: variable ya declarada: $id")
        }

        nodo.expresionInicial?.let { expr ->
            val tExpr = expr.aceptar(this)
            if (tipoDecl != Tipo.ERROR && tExpr != Tipo.ERROR && tExpr != tipoDecl) {
                errores.add("Error semántico: tipo incompatible: $id")
            }
        }

        return Tipo.VOID
    }

    override fun visit(nodo: NodoDeclaracionSpecial?): Tipo {
        val id = nodo?.identificador ?: ""
        val declaracionCorrecta = tablaDeSimbolos.declarar(id, Tipo.SPECIAL)
        if (!declaracionCorrecta) {
            errores.add("Error semántico: variable ya declarada: $id")
        }
        nodo?.valor?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoAsignacion?): Tipo {
        val id = nodo?.identificador ?: ""
        val tipoVar = tablaDeSimbolos.buscar(id)

        if (tipoVar == null) {
            errores.add("Error semántico: variable no declarada: $id")
            nodo?.expresion?.aceptar(this)
            return Tipo.ERROR
        }

        val tipoExpr = nodo?.expresion?.aceptar(this) ?: Tipo.ERROR
        if (tipoExpr != Tipo.ERROR && tipoExpr != tipoVar) {
            errores.add("Error semántico: tipo incompatible en asignación a $id")
            return Tipo.ERROR
        }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoSentenciaElemento?): Tipo {
        nodo?.elemento?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoIf?): Tipo {
        val tCond = nodo?.condicion?.aceptar(this) ?: Tipo.ERROR
        if (tCond != Tipo.ERROR && tCond != Tipo.NUMBER && tCond != Tipo.BOOLEAN) {
            errores.add("Error semántico: condición de if debe ser NUMBER o BOOLEAN")
        }
        nodo?.bloqueThen?.aceptar(this)
        nodo?.cola?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoElseIf?): Tipo {
        val tCond = nodo?.condicion?.aceptar(this) ?: Tipo.ERROR
        if (tCond != Tipo.ERROR && tCond != Tipo.NUMBER && tCond != Tipo.BOOLEAN) {
            errores.add("Error semántico: condición de elseif debe ser NUMBER o BOOLEAN")
        }
        nodo?.bloque?.aceptar(this)
        nodo?.siguiente?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoElse?): Tipo {
        nodo?.bloque?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoFinIf?): Tipo {
        return Tipo.VOID
    }

    override fun visit(nodo: NodoWhile?): Tipo {
        val tCond = nodo?.condicion?.aceptar(this) ?: Tipo.ERROR
        if (tCond != Tipo.ERROR && tCond != Tipo.NUMBER && tCond != Tipo.BOOLEAN) {
            errores.add("Error semántico: condición de while debe ser NUMBER o BOOLEAN")
        }
        nodo?.bloque?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoDoWhile?): Tipo {
        nodo?.bloque?.aceptar(this)
        val tCond = nodo?.condicion?.aceptar(this) ?: Tipo.ERROR
        if (tCond != Tipo.ERROR && tCond != Tipo.NUMBER && tCond != Tipo.BOOLEAN) {
            errores.add("Error semántico: condición de do-while debe ser NUMBER o BOOLEAN")
        }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoForClasico?): Tipo {
        nodo?.inicializacion?.aceptar(this)
        val tCond = nodo?.condicion?.aceptar(this) ?: Tipo.ERROR
        if (tCond != Tipo.ERROR && tCond != Tipo.NUMBER && tCond != Tipo.BOOLEAN) {
            errores.add("Error semántico: condición de for debe ser NUMBER o BOOLEAN")
        }
        nodo?.actualizacion?.aceptar(this)
        nodo?.bloque?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoForRango?): Tipo {
        val id = nodo?.identificador ?: ""
        if (id.isNotBlank()) {
            val ok = tablaDeSimbolos.declarar(id, Tipo.NUMBER)
            if (!ok) {
                errores.add("Error semántico: variable ya declarada: $id")
            }
        }

        val tIni = nodo?.inicio?.aceptar(this) ?: Tipo.ERROR
        val tFin = nodo?.fin?.aceptar(this) ?: Tipo.ERROR
        if (tIni != Tipo.ERROR && tIni != Tipo.NUMBER) {
            errores.add("Error semántico: inicio de rango no es NUMBER")
        }
        if (tFin != Tipo.ERROR && tFin != Tipo.NUMBER) {
            errores.add("Error semántico: fin de rango no es NUMBER")
        }
        nodo?.bloque?.aceptar(this)
        return Tipo.VOID
    }

    override fun visit(nodo: NodoExpresionBinaria?): Tipo? {
        val op = nodo?.operador ?: ""
        val tIzq = nodo?.izquierda?.aceptar(this) ?: Tipo.ERROR
        val tDer = nodo?.derecha?.aceptar(this) ?: Tipo.ERROR

        if (tIzq == Tipo.ERROR || tDer == Tipo.ERROR) return Tipo.ERROR

        return when (op) {
            "+", "-", "*", "/", "^", "%" -> {
                if (tIzq != Tipo.NUMBER || tDer != Tipo.NUMBER) {
                    errores.add("Error semántico: operación aritmética requiere NUMBER")
                    Tipo.ERROR
                } else Tipo.NUMBER
            }
            "&&", "||" -> {
                val izqOk = (tIzq == Tipo.BOOLEAN || tIzq == Tipo.NUMBER)
                val derOk = (tDer == Tipo.BOOLEAN || tDer == Tipo.NUMBER)
                if (!izqOk || !derOk) {
                    errores.add("Error semántico: operación lógica requiere operandos NUMBER o BOOLEAN")
                    Tipo.ERROR
                } else Tipo.BOOLEAN
            }
            "==", "!!", "<", ">", "<=", ">=" -> {
                if (tIzq != tDer) {
                    errores.add("Error semántico: comparación entre tipos incompatibles")
                    Tipo.ERROR
                } else Tipo.BOOLEAN
            }
            else -> {
                Tipo.ERROR
            }
        }
    }

    override fun visit(nodo: NodoExpresionUnaria?): Tipo? {
        val op = nodo?.operador ?: ""
        val t = nodo?.expresion?.aceptar(this) ?: Tipo.ERROR
        if (t == Tipo.ERROR) return Tipo.ERROR

        return when (op) {
            "~" -> {
                when (t) {
                    Tipo.BOOLEAN -> Tipo.BOOLEAN
                    Tipo.NUMBER -> Tipo.NUMBER
                    else -> {
                        errores.add("Error semántico: operador ~ requiere BOOLEAN o NUMBER")
                        Tipo.ERROR
                    }
                }
            }
            "-", "+" -> {
                if (t != Tipo.NUMBER) {
                    errores.add("Error semántico: operador requiere NUMBER")
                    Tipo.ERROR
                } else Tipo.NUMBER
            }
            else -> Tipo.ERROR
        }
    }

    override fun visit(nodo: NodoLiteral?): Tipo? {
        val t = nodo?.tipoLiteral ?: return Tipo.ERROR
        return try {
            Tipo.valueOf(t.uppercase())
        } catch (_: IllegalArgumentException) {
            Tipo.ERROR
        }
    }

    override fun visit(nodo: NodoIdentificador?): Tipo {
        val id = nodo?.nombre ?: ""
        val tipo = tablaDeSimbolos.buscar(id)
        if (tipo == null) {
            errores.add("Error semántico: variable no declarada: $id")
            return Tipo.ERROR
        }
        return tipo
    }

    override fun visit(nodo: NodoCadenaCompuesta?): Tipo {
        val partes = nodo?.partes ?: emptyList()
        for (p in partes) {
            p.aceptar(this)
        }
        return Tipo.STRING
    }

    override fun visit(nodo: NodoLlamadaMetodo?): Tipo {
        val args = nodo?.argumentos
        args?.forEach { (it as? NodoExpresion)?.aceptar(this) }
        return Tipo.VOID
    }

    override fun visit(nodo: NodoComodin?): Tipo {
        return Tipo.VOID
    }


}