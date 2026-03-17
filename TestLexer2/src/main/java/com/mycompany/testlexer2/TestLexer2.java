package com.mycompany.testlexer2;

import com.mycompany.testlexer2.ast.AST;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestLexer2 {

    public static void main(String[] args) {
        try {
            //String ruta = "/home/mynordma/testcompi/l3.2valido.txt";
            //String ruta = "/home/mynordma/testcompi/l3invalido.txt";
            String ruta = "/home/mynordma/testcompi/l4.2valido.txt";
            // String ruta = "/home/mynordma/testcompi/l4invalido.txt";
            
            ManejadorAnalisis manejadorAnalisis = new ManejadorAnalisis();
            AST ast = manejadorAnalisis.analizarPKM(new FileReader(ruta));
            //AST ast = manejadorAnalisis.analizarForm(new FileReader(ruta));
            ast.imprimir();
            System.out.println(manejadorAnalisis.getReporteGeneral());
            //System.out.println(manejadorAnalisis.getReporteErrores());
            
        } catch (FileNotFoundException ex) {
            System.getLogger(TestLexer2.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}