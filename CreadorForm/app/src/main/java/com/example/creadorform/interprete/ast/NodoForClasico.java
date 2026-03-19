/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.creadorform.interprete.ast;

import com.example.creadorform.interprete.Visitor;

/**
 *
 * @author mynordma
 */
public class NodoForClasico extends NodoSentencia {
    
    private NodoAsignacion inicializacion;
    private NodoExpresion condicion;
    private NodoAsignacion actualizacion;
    private NodoBloque bloque;

    public NodoForClasico(NodoAsignacion inicializacion, NodoExpresion condicion, NodoAsignacion actualizacion, NodoBloque bloque, int linea, int columna) {
        super(linea, columna);
        this.inicializacion = inicializacion;
        this.condicion = condicion;
        this.actualizacion = actualizacion;
        this.bloque = bloque;
    }

    public NodoAsignacion getInicializacion() {
        return inicializacion;
    }

    public void setInicializacion(NodoAsignacion inicializacion) {
        this.inicializacion = inicializacion;
    }

    public NodoExpresion getCondicion() {
        return condicion;
    }

    public void setCondicion(NodoExpresion condicion) {
        this.condicion = condicion;
    }

    public NodoAsignacion getActualizacion() {
        return actualizacion;
    }

    public void setActualizacion(NodoAsignacion actualizacion) {
        this.actualizacion = actualizacion;
    }

    public NodoBloque getBloque() {
        return bloque;
    }

    public void setBloque(NodoBloque bloque) {
        this.bloque = bloque;
    }

    @Override
    public <T> T aceptar(Visitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo, "NodoForClasico");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        imprimirEncabezado(nuevoPrefijo, false, "Inicializacion");
        String prefijoInit = prefijoHijo(nuevoPrefijo, false);
        if (inicializacion != null) {
            inicializacion.imprimir(prefijoInit, true);
        }

        imprimirEncabezado(nuevoPrefijo, false, "Condicion");
        String prefijoCond = prefijoHijo(nuevoPrefijo, false);
        if (condicion != null) {
            condicion.imprimir(prefijoCond, true);
        }

        imprimirEncabezado(nuevoPrefijo, false, "Actualizacion");
        String prefijoAct = prefijoHijo(nuevoPrefijo, false);
        if (actualizacion != null) {
            actualizacion.imprimir(prefijoAct, true);
        }

        imprimirEncabezado(nuevoPrefijo, true, "Bloque");
        String prefijoBloque = prefijoHijo(nuevoPrefijo, true);
        if (bloque != null) {
            bloque.imprimir(prefijoBloque, true);
        }
    }
}
