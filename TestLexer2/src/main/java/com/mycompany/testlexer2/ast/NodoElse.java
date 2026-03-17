/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoElse extends NodoColaIf {
    
    private NodoBloque bloque;

    public NodoElse(NodoBloque bloque, int linea, int columna) {
        super(linea, columna);
        this.bloque = bloque;
    }

    public NodoBloque getBloque() {
        return bloque;
    }

    public void setBloque(NodoBloque bloque) {
        this.bloque = bloque;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoElse");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        if (bloque != null) {
            bloque.imprimir(nuevoPrefijo, true);
        }
    }
}
