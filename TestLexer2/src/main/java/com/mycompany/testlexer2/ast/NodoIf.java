/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoIf extends NodoSentencia {
    
    private NodoExpresion condicion;
    private NodoBloque bloqueThen;
    private NodoColaIf cola;

    public NodoIf(NodoExpresion condicion, NodoBloque bloqueThen, NodoColaIf cola, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.bloqueThen = bloqueThen;
        this.cola = cola;
    }

    public NodoExpresion getCondicion() {
        return condicion;
    }

    public void setCondicion(NodoExpresion condicion) {
        this.condicion = condicion;
    }

    public NodoBloque getBloqueThen() {
        return bloqueThen;
    }

    public void setBloqueThen(NodoBloque bloqueThen) {
        this.bloqueThen = bloqueThen;
    }

    public NodoColaIf getCola() {
        return cola;
    }

    public void setCola(NodoColaIf cola) {
        this.cola = cola;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoIf");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        imprimirEncabezado(nuevoPrefijo, false, "Condicion");
        String prefijoCond = prefijoHijo(nuevoPrefijo, false);
        if (condicion != null) {
            condicion.imprimir(prefijoCond, true);
        }

        imprimirEncabezado(nuevoPrefijo, false, "Then");
        String prefijoThen = prefijoHijo(nuevoPrefijo, false);
        if (bloqueThen != null) {
            bloqueThen.imprimir(prefijoThen, true);
        }

        imprimirEncabezado(nuevoPrefijo, true, "ColaIf");
        String prefijoCola = prefijoHijo(nuevoPrefijo, true);
        if (cola != null) {
            cola.imprimir(prefijoCola, true);
        }
    }
}
