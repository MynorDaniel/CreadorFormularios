/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoDoWhile extends NodoSentencia {
    
    private NodoExpresion condicion;
    private NodoBloque bloque;

    public NodoDoWhile(NodoBloque bloque, NodoExpresion condicion, int linea, int columna) {
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
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoDoWhile");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        imprimirEncabezado(nuevoPrefijo, false, "Bloque");
        String prefijoBloque = prefijoHijo(nuevoPrefijo, false);
        if (bloque != null) {
            bloque.imprimir(prefijoBloque, true);
        }

        imprimirEncabezado(nuevoPrefijo, true, "Condicion");
        String prefijoCond = prefijoHijo(nuevoPrefijo, true);
        if (condicion != null) {
            condicion.imprimir(prefijoCond, true);
        }
    }
}
