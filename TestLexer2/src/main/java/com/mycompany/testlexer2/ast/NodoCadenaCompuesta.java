/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

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
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoCadenaCompuesta");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < partes.size(); i++) {
            partes.get(i).imprimir(nuevoPrefijo, i == partes.size() - 1);
        }
    }

}