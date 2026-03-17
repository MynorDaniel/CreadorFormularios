/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public abstract class NodoAST {
    
    private int linea;
    private int columna;

    public NodoAST(int linea, int columna) {
        this.linea = linea;
        this.columna = columna;
    }

    public NodoAST() {
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
    
    public final void imprimir() {
        imprimir("", true);
    }

    protected abstract void imprimir(String prefijo, boolean esUltimo);

    protected void imprimirEncabezado(String prefijo, boolean esUltimo, String texto) {
        System.out.println(prefijo + (esUltimo ? "└── " : "├── ") + texto);
    }

    protected String prefijoHijo(String prefijo, boolean esUltimo) {
        return prefijo + (esUltimo ? "    " : "│   ");
    }
}
