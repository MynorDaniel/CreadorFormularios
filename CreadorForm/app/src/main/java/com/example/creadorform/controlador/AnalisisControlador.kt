package com.example.creadorform.controlador

import com.example.creadorform.dominio.Formulario
import com.example.creadorform.interprete.ManejadorAnalisis
import com.example.creadorform.interprete.analizadorsemantico.AnalizadorSemantico
import com.example.creadorform.interprete.ast.AST
import java.io.StringReader

class AnalisisControlador {

    private var _errores = String()
    val errores: String get() = _errores

    fun analizarForm(entrada: String): Formulario{

        // Analisis lexico y sintactico
        val manejador: ManejadorAnalisis = ManejadorAnalisis()
        val ast: AST = manejador.analizarForm(StringReader(entrada))
        ast.imprimir()

        // Analisis semantico
        val analizadorSemantico : AnalizadorSemantico = AnalizadorSemantico()
        analizadorSemantico.analizarSemantica(ast)

        _errores = manejador.reporteGeneral


        // Creacion de la entrada
        val formulario: Formulario = Formulario()
        return formulario
    }
}