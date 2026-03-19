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
public class NodoBloque extends NodoAST {
    
    private List<NodoSentencia> sentencias;

    public NodoBloque(List<NodoSentencia> sentencias, int linea, int columna) {
        super(linea, columna);
        this.sentencias = sentencias;
    }

    public NodoBloque() {
    }

    public List<NodoSentencia> getSentencias() {
        return sentencias;
    }

    public void setSentencias(List<NodoSentencia> sentencias) {
        this.sentencias = sentencias;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoBloque");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < sentencias.size(); i++) {
            sentencias.get(i).imprimir(nuevoPrefijo, i == sentencias.size() - 1);
        }
    }
}
