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
public class NodoSeccion extends NodoElemento {
    
    private List<NodoAtributo> atributos;

    public NodoSeccion(List<NodoAtributo> atributos, int linea, int columna) {
        super(linea, columna);
        this.atributos = atributos;
    }

    public List getAtributos() {
        return atributos;
    }

    public void setAtributos(List atributos) {
        this.atributos = atributos;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoSeccion");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < atributos.size(); i++) {
            atributos.get(i).imprimir(nuevoPrefijo, i == atributos.size() - 1);
        }
    }
}
