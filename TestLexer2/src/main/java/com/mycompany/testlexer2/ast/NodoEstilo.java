/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoEstilo extends NodoAST {
    
    private String clave;
    private NodoExpresion valor;

    public NodoEstilo(String clave, NodoExpresion valor, int linea, int columna) {
        super(linea, columna);
        this.clave = clave;
        this.valor = valor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public NodoExpresion getValor() {
        return valor;
    }

    public void setValor(NodoExpresion valor) {
        this.valor = valor;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoEstilo(clave=" + clave + ")");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        if (valor != null) {
            valor.imprimir(nuevoPrefijo, true);
        }
    }
}
