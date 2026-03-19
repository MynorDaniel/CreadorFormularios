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
public class NodoAtributo extends NodoAST {
    
    private String nombre;
    private NodoAST valor;

    public NodoAtributo(String nombre, NodoAST valor, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public NodoAST getValor() {
        return valor;
    }

    public void setValor(NodoAST valor) {
        this.valor = valor;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoAtributo(nombre=" + nombre + ")");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        if (valor != null) {
            valor.imprimir(nuevoPrefijo, true);
        }
    }
}
