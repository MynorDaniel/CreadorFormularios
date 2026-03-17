/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoExpresionBinaria extends NodoExpresion {
    
    private String operador;
    private NodoExpresion izquierda;
    private NodoExpresion derecha;

    public NodoExpresionBinaria(String operador, NodoExpresion izquierda, NodoExpresion derecha, int linea, int columna) {
        super(linea, columna);
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public NodoExpresion getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(NodoExpresion izquierda) {
        this.izquierda = izquierda;
    }

    public NodoExpresion getDerecha() {
        return derecha;
    }

    public void setDerecha(NodoExpresion derecha) {
        this.derecha = derecha;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo,
                "NodoExpresionBinaria(op=" + operador + ")");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        if (izquierda != null) {
            izquierda.imprimir(nuevoPrefijo, false);
        }
        if (derecha != null) {
            derecha.imprimir(nuevoPrefijo, true);
        }
    }
}
