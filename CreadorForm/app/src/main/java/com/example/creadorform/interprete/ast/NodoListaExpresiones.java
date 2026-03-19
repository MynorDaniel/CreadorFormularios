/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.creadorform.interprete.ast;

import com.example.creadorform.interprete.Visitor;
import java.util.List;

/**
 *
 * @author mynordma
 */
public class NodoListaExpresiones extends NodoAST {
    
    private List<NodoExpresion> expresiones;

    public NodoListaExpresiones(List expresiones, int linea, int columna) {
        super(linea, columna);
        this.expresiones = expresiones;
    }

    public List<NodoExpresion> getExpresiones() {
        return expresiones;
    }

    public void setExpresiones(List<NodoExpresion> expresiones) {
        this.expresiones = expresiones;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoListaExpresiones");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < expresiones.size(); i++) {
            expresiones.get(i).imprimir(nuevoPrefijo, i == expresiones.size() - 1);
        }
    }
}
