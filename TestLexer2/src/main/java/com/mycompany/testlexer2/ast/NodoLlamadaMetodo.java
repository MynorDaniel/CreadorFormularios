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
public class NodoLlamadaMetodo extends NodoSentencia {
    
    private String objetivo;
    private String metodo;
    private List<NodoExpresion> argumentos;

    public NodoLlamadaMetodo(String objetivo, String metodo, List argumentos, int linea, int columna) {
        super(linea, columna);
        this.objetivo = objetivo;
        this.metodo = metodo;
        this.argumentos = argumentos;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public List getArgumentos() {
        return argumentos;
    }

    public void setArgumentos(List argumentos) {
        this.argumentos = argumentos;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo,
            "NodoLlamadaMetodo(objetivo=" + objetivo + ", metodo=" + metodo + ")");
        String nuevoPrefijo = prefijoHijo(prefijo, esUltimo);

        for (int i = 0; i < argumentos.size(); i++) {
            argumentos.get(i).imprimir(nuevoPrefijo, i == argumentos.size() - 1);
        }
    }
}
