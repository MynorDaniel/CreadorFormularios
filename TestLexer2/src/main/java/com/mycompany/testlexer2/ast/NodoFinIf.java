/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoFinIf extends NodoColaIf {
    
    public NodoFinIf(int linea, int columna) {
        super(linea, columna);
    }

    public NodoFinIf() {
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoFinIf");
    }
}
