/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projetcompilwhilejas;

/**
 *
 * @author USER
 */
 import java.util.List;

public class ProjetCompilWhileJaS{
   
    public static void main(String[] args) {

        // --- TON CODE A TESTER ---
        String code = """
                      
           let x = 0;
if(x>2){x=1+x;}
            while(x >5) {
                x = x+ 1;
            }

            // fin
        """;

        try {
            // 1) Analyse lexicale
            AnalyseurLexical lex = new AnalyseurLexical(code);
            var tokens = lex.analyser();

            // 2) Analyse syntaxique
            AnalyseurSyntaxique syn = new AnalyseurSyntaxique(tokens);
            
            syn.Z();  
 
        }
        catch (Exception e) {
            System.out.println(" Erreur non geree : " + e.getMessage());
        }
    }
}

 