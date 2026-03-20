/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.creadorform.interprete.parser;

import com.example.creadorform.interprete.ast.*;
import java.io.Reader;
import java_cup.runtime.Symbol;

/**
 *
 * @author mynordma
 */
public class ManejadorAnalisis {
    
    private String reporteGeneral;
    private String reporteErrores;
    
    public AST analizarForm(Reader reader){
        reporteGeneral = "";
        reporteErrores = "";
        
        LexerForm lexer = new LexerForm(reader);
        ParserForm parser = new ParserForm(lexer);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder sbErrores = new StringBuilder();
        try {
            
            AST ast = parsear(parser);
            
            if (!parser.getErroresSintacticos().isEmpty()) {
                sb.append("Errores sintácticos:");
                sb.append(System.lineSeparator());
                sbErrores.append("Errores sintácticos:");
                sbErrores.append(System.lineSeparator());
                for (String err : parser.getErroresSintacticos()) {
                    sb.append(err);
                    sb.append(System.lineSeparator());
                    sbErrores.append(err);
                    sbErrores.append(System.lineSeparator());
                }
            }
            generarReporteLexer(lexer, sb, sbErrores);
            reporteGeneral = sb.toString();
            return ast;
        } catch (Exception ex) {
            sb.append("Error durante el análisis sintáctico:");
            sb.append(System.lineSeparator());
            sb.append(ex.getMessage());
            sb.append(System.lineSeparator());
            generarReporteLexer(lexer, sb, sbErrores);
            reporteGeneral = sb.toString();
            return new AST(null);
        }
    }
    
    public AST analizarPKM(Reader reader){
        reporteGeneral = "";
        reporteErrores = "";
        LexerPKM lexer = new LexerPKM(reader);
        ParserPKM parser = new ParserPKM(lexer);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder sbErrores = new StringBuilder();
        try {
            
            AST ast = parsear(parser);
            
            if (!parser.getErroresSintacticos().isEmpty()) {
                sb.append("Errores sintácticos:");
                sb.append(System.lineSeparator());
                sbErrores.append("Errores sintácticos:");
                sbErrores.append(System.lineSeparator());
                for (String err : parser.getErroresSintacticos()) {
                    sb.append(err);
                    sb.append(System.lineSeparator());
                    sbErrores.append(err);
                    sbErrores.append(System.lineSeparator());
                }
            }
            generarReporteLexer(lexer, sb, sbErrores);
            reporteGeneral = sb.toString();
            return ast;
        } catch (Exception ex) {
            sb.append("Error durante el análisis sintáctico:");
            sb.append(System.lineSeparator());
            sb.append(ex.getMessage());
            sb.append(System.lineSeparator());
            generarReporteLexer(lexer, sb, sbErrores);
            reporteGeneral = sb.toString();
            
            return new AST(null);
        }
    }
    
    private AST parsear(java_cup.runtime.lr_parser parser) throws Exception {
        Symbol resultado;
        resultado = parser.parse();

        if (resultado != null && resultado.value instanceof NodoPrograma) {
            NodoPrograma raiz = (NodoPrograma) resultado.value;
            return new AST(raiz);
        }
        return new AST(null);
    }
    
    private void generarReporteLexer(Lexer lexer, StringBuilder sb, StringBuilder sbErrores){
        sb.append("Reporte análisis léxico:");
        sb.append(System.lineSeparator());

        for (Token t : lexer.getTokens()) {
            sb.append(t.tipo).append(" | ").append(t.lexema).append(" | ").append(t.linea).append(":").append(t.columna);
            sb.append(System.lineSeparator());
        }

        for (Token e : lexer.getErrores()) {
            sb.append("Error léxico -> ").append(e.lexema).append(" | ").append(e.linea).append(":").append(e.columna);
            sb.append(System.lineSeparator());
            
            sbErrores.append("Error léxico -> ").append(e.lexema).append(" | ").append(e.linea).append(":").append(e.columna);
            sbErrores.append(System.lineSeparator());
        }
    }

    public String getReporteGeneral() {
        return reporteGeneral;
    }

    public void setReporteGeneral(String reporte) {
        this.reporteGeneral = reporte;
    }

    public String getReporteErrores() {
        return reporteErrores;
    }

    public void setReporteErrores(String reporteErrores) {
        this.reporteErrores = reporteErrores;
    }
}
