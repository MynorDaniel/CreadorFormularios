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
public class NodoEstilos extends NodoAST {
    
    private List<NodoEstilo> estilos;

    public NodoEstilos(List<NodoEstilo> estilos, int linea, int columna) {
        super(linea, columna);
        this.estilos = estilos;
    }

    public List<NodoEstilo> getEstilos() {
        return estilos;
    }

    public void setEstilos(List<NodoEstilo> estilos) {
        this.estilos = estilos;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoEstilos");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < estilos.size(); i++) {
            estilos.get(i).imprimir(nuevoPrefijo, i == estilos.size() - 1);
        }
    }
}
