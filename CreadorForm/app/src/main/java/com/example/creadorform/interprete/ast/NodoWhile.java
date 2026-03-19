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
public class NodoWhile extends NodoSentencia {
    
    private NodoExpresion condicion;
    private NodoBloque bloque;

    public NodoWhile(NodoExpresion condicion, NodoBloque bloque, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.bloque = bloque;
    }

    public NodoExpresion getCondicion() {
        return condicion;
    }

    public void setCondicion(NodoExpresion condicion) {
        this.condicion = condicion;
    }

    public NodoBloque getBloque() {
        return bloque;
    }

    public void setBloque(NodoBloque bloque) {
        this.bloque = bloque;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoWhile");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        imprimirEncabezado(nuevoPrefijo, false, "Condicion");
        String prefijoCond = prefijoHijo(nuevoPrefijo, false);
        if (condicion != null) {
            condicion.imprimir(prefijoCond, true);
        }

        imprimirEncabezado(nuevoPrefijo, true, "Bloque");
        String prefijoBloque = prefijoHijo(nuevoPrefijo, true);
        if (bloque != null) {
            bloque.imprimir(prefijoBloque, true);
        }
    }
}
