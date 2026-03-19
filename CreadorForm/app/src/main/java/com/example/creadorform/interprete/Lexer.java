/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.creadorform.interprete;

import java.util.List;

/**
 *
 * @author mynordma
 */
public abstract class Lexer {
    
    public abstract List<Token> getTokens();

    public abstract List<Token> getErrores();

}
