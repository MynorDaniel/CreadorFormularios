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
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
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
