/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

import java.util.List;

/**
 *
 * @author mynordma
 */
public class NodoPrograma extends NodoAST {
    
    private List<NodoSentencia> sentencias;
    private List<NodoElemento> elementos;

    public NodoPrograma(List<NodoSentencia> sentencias, int linea, int columna) {
        super(linea, columna);
        this.sentencias = sentencias;
    }
    
    public NodoPrograma(List<NodoElemento> elementos, boolean x, int linea, int columna) {
        super(linea, columna);
        this.elementos = elementos;
    }

    public NodoPrograma() {
    }

    public List<NodoSentencia> getSentencias() {
        return sentencias;
    }

    public void setSentencias(List<NodoSentencia> sentencias) {
        this.sentencias = sentencias;
    }

    public List<NodoElemento> getElementos() {
        return elementos;
    }

    public void setElementos(List<NodoElemento> elementos) {
        this.elementos = elementos;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoPrograma");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);
        
        if(sentencias != null){
            for (int i = 0; i < sentencias.size(); i++) {
                sentencias.get(i).imprimir(nuevoPrefijo, i == sentencias.size() - 1);
            }
        }

        if(elementos != null){
            for (int i = 0; i < elementos.size(); i++) {
                elementos.get(i).imprimir(nuevoPrefijo, i == elementos.size() - 1);
            }
        }
        
        
    }
}
