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
public class NodoListaElementos extends NodoAST {
    
    private List<NodoElemento> elementos;

    public NodoListaElementos(List<NodoElemento> elementos, int linea, int columna) {
        super(linea, columna);
        this.elementos = elementos;
    }

    public List<NodoElemento> getElementos() {
        return elementos;
    }

    public void setElementos(List<NodoElemento> elementos) {
        this.elementos = elementos;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoListaElementos");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < elementos.size(); i++) {
            if(elementos.get(i) != null){
                elementos.get(i).imprimir(nuevoPrefijo, i == elementos.size() - 1);
            }
        }
    }
}
