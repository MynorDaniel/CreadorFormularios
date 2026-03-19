/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.creadorform.interprete.ast;

import com.example.creadorform.interprete.Visitor;

/**
 *
 * @author mynordma
 */
public class NodoExpresionUnaria extends NodoExpresion {
    
    private String operador;
    private NodoExpresion expresion;

    public NodoExpresionUnaria(String operador, NodoExpresion expresion, int linea, int columna) {
        super(linea, columna);
        this.operador = operador;
        this.expresion = expresion;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }

    public void setExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo,
                "NodoExpresionUnaria(op=" + operador + ")");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        if (expresion != null) {
            expresion.imprimir(nuevoPrefijo, true);
        }
    }
}
