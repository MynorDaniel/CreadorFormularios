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
public class NodoFinIf extends NodoColaIf {
    
    public NodoFinIf(int linea, int columna) {
        super(linea, columna);
    }

    public NodoFinIf() {
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoFinIf");
    }
}
