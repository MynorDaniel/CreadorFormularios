package com.example.creadorform.controlador

import com.example.creadorform.dominio.Formulario
import com.example.creadorform.interprete.Evaluador
import com.example.creadorform.interprete.parser.ManejadorAnalisis
import com.example.creadorform.interprete.analizadorsemantico.AnalizadorSemantico
import com.example.creadorform.interprete.ast.AST
import com.example.creadorform.interprete.ast.NodoPrograma
import java.io.StringReader

class AnalisisControlador {

    private var _errores = String()
    val errores: String get() = _errores

    fun analizarForm(entrada: String): Formulario{

        try {
            if (entrada.isBlank()) {
                throw IllegalArgumentException("La entrada no puede estar vacía.")
            }

            // Analisis lexico y sintactico
            val manejador: ManejadorAnalisis = ManejadorAnalisis()
            val ast: AST = manejador.analizarForm(StringReader(entrada))
            ast.imprimir()

            // Analisis semantico
            val analizadorSemantico : AnalizadorSemantico = AnalizadorSemantico()
            analizadorSemantico.analizarSemantica(ast)

            val erroresSemanticos = analizadorSemantico.errores.joinToString(separator = "\n")

            val sb = StringBuilder()
            _errores = sb.append(manejador.reporteGeneral)
                .append("\n")
                .append(erroresSemanticos)
                .toString()

            if (manejador.reporteGeneral.isNotBlank() || analizadorSemantico.errores.isNotEmpty()) {
                return Formulario()
            }

            val evaluador = Evaluador()
            return ast.raiz?.let { evaluador.evaluar(it as NodoPrograma) } ?: Formulario()
        }catch (e: Exception) {
            _errores = e.message ?: "Error desconocido"
            e.printStackTrace()
            return Formulario()
        }

    }
}