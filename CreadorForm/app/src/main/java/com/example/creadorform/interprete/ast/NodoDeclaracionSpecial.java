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
public class NodoDeclaracionSpecial extends NodoSentencia {
    
    private String identificador;
    private NodoPregunta valor;

    public NodoDeclaracionSpecial(String identificador, NodoPregunta valor, int linea, int columna) {
        super(linea, columna);
        this.identificador = identificador;
        this.valor = valor;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public NodoPregunta getValor() {
        return valor;
    }

    public void setValor(NodoPregunta valor) {
        this.valor = valor;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
protected void imprimir(String prefijo, boolean esUltimo) {
    imprimirEncabezado(prefijo, esUltimo,
        "NodoDeclaracionSpecial(id=" + identificador + ")");
    String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

    if (valor != null) {
        valor.imprimir(nuevoPrefijo, true);
    }
}
}
