/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.testlexer2.ast;

/**
 *
 * @author mynordma
 */
public class AST {
    
    private NodoAST raiz;

    public AST(NodoAST raiz) {
        this.raiz = raiz;
    }

    public NodoAST getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoAST raiz) {
        this.raiz = raiz;
    }
    
    public void imprimir() {
        if (raiz != null) {
            raiz.imprimir();
        } else {
            System.out.println("(AST vacío)");
        }
    }
}
