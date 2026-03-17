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
public class NodoPreguntaDesplegable extends NodoPregunta {
    
    private List<NodoAtributo> atributos;

    public NodoPreguntaDesplegable(List atributos, int linea, int columna) {
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
        imprimirEncabezado(prefijo, esUltimo, "NodoPreguntaDesplegable");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < atributos.size(); i++) {
            atributos.get(i).imprimir(nuevoPrefijo, i == atributos.size() - 1);
        }
    }
}
