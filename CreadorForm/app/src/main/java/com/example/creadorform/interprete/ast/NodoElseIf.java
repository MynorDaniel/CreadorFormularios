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
public class NodoElseIf extends NodoColaIf {

    private NodoExpresion condicion;
    private NodoBloque bloque;
    private NodoColaIf siguiente;

    public NodoElseIf(NodoExpresion condicion, NodoBloque bloque, NodoColaIf siguiente, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.bloque = bloque;
        this.siguiente = siguiente;
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

    public NodoColaIf getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoColaIf siguiente) {
        this.siguiente = siguiente;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoElseIf");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        imprimirEncabezado(nuevoPrefijo, false, "Condicion");
        String prefijoCond = prefijoHijo(nuevoPrefijo, false);
        if (condicion != null) {
            condicion.imprimir(prefijoCond, true);
        }

        imprimirEncabezado(nuevoPrefijo, false, "Bloque");
        String prefijoBloque = prefijoHijo(nuevoPrefijo, false);
        if (bloque != null) {
            bloque.imprimir(prefijoBloque, true);
        }

        imprimirEncabezado(nuevoPrefijo, true, "Siguiente");
        String prefijoSig = prefijoHijo(nuevoPrefijo, true);
        if (siguiente != null) {
            siguiente.imprimir(prefijoSig, true);
        }
    }

}
