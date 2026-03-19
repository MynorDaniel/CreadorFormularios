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
public class NodoDeclaracionVariable extends NodoSentencia {
    
    private String tipo;
    private String identificador;
    private NodoExpresion expresionInicial;

    public NodoDeclaracionVariable(String tipo, String identificador, NodoExpresion expresionInicial, int linea, int columna) {
        super(linea, columna);
        this.tipo = tipo;
        this.identificador = identificador;
        this.expresionInicial = expresionInicial;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public NodoExpresion getExpresionInicial() {
        return expresionInicial;
    }

    public void setExpresionInicial(NodoExpresion expresionInicial) {
        this.expresionInicial = expresionInicial;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
protected void imprimir(String prefijo, boolean esUltimo) {
    imprimirEncabezado(prefijo, esUltimo,
        "NodoDeclaracionVariable(tipo=" + tipo + ", id=" + identificador + ")");
    String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

    if (expresionInicial != null) {
        expresionInicial.imprimir(nuevoPrefijo, true);
    }
}
}
