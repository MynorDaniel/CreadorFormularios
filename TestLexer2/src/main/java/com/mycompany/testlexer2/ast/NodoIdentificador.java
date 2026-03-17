/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class NodoIdentificador extends NodoExpresion {
    
    private String nombre;

    public NodoIdentificador(String nombre, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    protected void imprimir(String prefijo, boolean esUltimo) {
        imprimirEncabezado(prefijo, esUltimo,
                "NodoIdentificador(nombre=" + nombre + ")");
    }
}
