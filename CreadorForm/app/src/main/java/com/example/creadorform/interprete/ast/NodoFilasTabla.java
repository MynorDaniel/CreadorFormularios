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
public class NodoFilasTabla extends NodoAST {
    
    private List<List<NodoElemento>> filas;

    public NodoFilasTabla(List<List<NodoElemento>> filas, int linea, int columna) {
        super(linea, columna);
        this.filas = filas;
    }

    public List<List<NodoElemento>> getFilas() {
        return filas;
    }

    public void setFilas(List<List<NodoElemento>> filas) {
        this.filas = filas;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoFilasTabla");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < filas.size(); i++) {
            List<NodoElemento> fila = filas.get(i);
            boolean ultimaFila = (i == filas.size() - 1);

            imprimirEncabezado(nuevoPrefijo, ultimaFila, "Fila " + (i + 1));
            String prefijoFila = prefijoHijo(nuevoPrefijo, ultimaFila);

            for (int j = 0; j < fila.size(); j++) {
                fila.get(j).imprimir(prefijoFila, j == fila.size() - 1);
            }
        }
    }
}
