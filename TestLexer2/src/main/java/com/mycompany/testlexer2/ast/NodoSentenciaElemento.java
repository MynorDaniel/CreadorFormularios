/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoSentenciaElemento extends NodoSentencia {
    
    private NodoElemento elemento;

    public NodoSentenciaElemento(NodoElemento elemento, int linea, int columna) {
        super(linea, columna);
        this.elemento = elemento;
    }

    public NodoElemento getElemento() {
        return elemento;
    }

    public void setElemento(NodoElemento elemento) {
        this.elemento = elemento;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoSentenciaElemento");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        if (elemento != null) {
            elemento.imprimir(nuevoPrefijo, true);
        }
    }
}
