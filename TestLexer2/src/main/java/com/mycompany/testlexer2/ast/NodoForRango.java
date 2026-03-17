/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoForRango extends NodoSentencia {
    
    private String identificador;
    private NodoExpresion inicio;
    private NodoExpresion fin;
    private NodoBloque bloque;

    public NodoForRango(String identificador, NodoExpresion inicio, NodoExpresion fin, NodoBloque bloque, int linea, int columna) {
        super(linea, columna);
        this.identificador = identificador;
        this.inicio = inicio;
        this.fin = fin;
        this.bloque = bloque;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public NodoExpresion getInicio() {
        return inicio;
    }

    public void setInicio(NodoExpresion inicio) {
        this.inicio = inicio;
    }

    public NodoExpresion getFin() {
        return fin;
    }

    public void setFin(NodoExpresion fin) {
        this.fin = fin;
    }

    public NodoBloque getBloque() {
        return bloque;
    }

    public void setBloque(NodoBloque bloque) {
        this.bloque = bloque;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo,
            "NodoForRango(id=" + identificador + ")");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        imprimirEncabezado(nuevoPrefijo, false, "Inicio");
        String prefijoInicio = prefijoHijo(nuevoPrefijo, false);
        if (inicio != null) {
            inicio.imprimir(prefijoInicio, true);
        }

        imprimirEncabezado(nuevoPrefijo, false, "Fin");
        String prefijoFin = prefijoHijo(nuevoPrefijo, false);
        if (fin != null) {
            fin.imprimir(prefijoFin, true);
        }

        imprimirEncabezado(nuevoPrefijo, true, "Bloque");
        String prefijoBloque = prefijoHijo(nuevoPrefijo, true);
        if (bloque != null) {
            bloque.imprimir(prefijoBloque, true);
        }
    }
}
