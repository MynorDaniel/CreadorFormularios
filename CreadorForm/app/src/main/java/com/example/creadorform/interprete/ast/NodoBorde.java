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
public class NodoBorde extends NodoEstilo {
    
    private String clave;
    private NodoExpresion num;
    private NodoExpresion tipo;
    private NodoExpresion color;

    public NodoBorde(String clave, NodoExpresion num, NodoExpresion tipo, NodoExpresion color, int linea, int columna) {
        super(clave, num, linea, columna);
        this.clave = clave;
        this.num = num;
        this.tipo = tipo;
        this.color = color;
    }

    @Override
    public String getClave() {
        return clave;
    }

    @Override
    public void setClave(String clave) {
        this.clave = clave;
    }

    public NodoExpresion getNum() {
        return num;
    }

    public void setNum(NodoExpresion num) {
        this.num = num;
    }

    public NodoExpresion getTipo() {
        return tipo;
    }

    public void setTipo(NodoExpresion tipo) {
        this.tipo = tipo;
    }

    public NodoExpresion getColor() {
        return color;
    }

    public void setColor(NodoExpresion color) {
        this.color = color;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoBorde(clave=" + clave + ")");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        if (num != null) {
            num.imprimir(nuevoPrefijo, false);
        }
        
        if (tipo != null) {
            tipo.imprimir(nuevoPrefijo, false);
        }
        
        if (color != null) {
            color.imprimir(nuevoPrefijo, true);
        }
    }
}
