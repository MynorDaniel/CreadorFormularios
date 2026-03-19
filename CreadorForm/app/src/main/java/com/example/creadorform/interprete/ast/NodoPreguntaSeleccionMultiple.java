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
public class NodoPreguntaSeleccionMultiple extends NodoPregunta {
    
    private List<NodoAtributo> atributos;

    public NodoPreguntaSeleccionMultiple(List atributos, int linea, int columna) {
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
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoPreguntaSeleccionMultiple");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < atributos.size(); i++) {
            atributos.get(i).imprimir(nuevoPrefijo, i == atributos.size() - 1);
        }
    }
}
