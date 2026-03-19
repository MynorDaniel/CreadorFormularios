/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.creadorform.interprete.ast;

import com.example.creadorform.interprete.Visitor;
import java.util.List;

public class NodoCadenaCompuesta extends NodoExpresion {

    private final List<NodoExpresion> partes;

    public NodoCadenaCompuesta(List<NodoExpresion> partes, int linea, int columna) {
        super(linea, columna);
        this.partes = partes;
    }


    public List<NodoExpresion> getPartes() {
        return partes;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoCadenaCompuesta");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < partes.size(); i++) {
            partes.get(i).imprimir(nuevoPrefijo, i == partes.size() - 1);
        }
    }

}