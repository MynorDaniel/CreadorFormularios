/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoLiteral extends NodoExpresion {
    
    private String tipoLiteral;
    private String valor;

    public NodoLiteral(String tipoLiteral, String valor, int linea, int columna) {
        super(linea, columna);
        this.tipoLiteral = tipoLiteral;
        this.valor = valor;
    }

    public String getTipoLiteral() {
        return tipoLiteral;
    }

    public void setTipoLiteral(String tipoLiteral) {
        this.tipoLiteral = tipoLiteral;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo,
                "NodoLiteral(tipo=" + tipoLiteral + ", valor=" + valor + ")");
    }
}
