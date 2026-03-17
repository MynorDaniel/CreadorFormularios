/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoAsignacion extends NodoSentencia {
    
    private String identificador;
    private NodoExpresion expresion;
    
    public NodoAsignacion(String identificador, NodoExpresion expresion, int linea, int columna) {
        super(linea, columna);
        this.identificador = identificador;
        this.expresion = expresion;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }

    public void setExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo,
                "NodoAsignacion(id=" + identificador + ")");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        if (expresion != null) {
            expresion.imprimir(nuevoPrefijo, true);
        }
    }

}
