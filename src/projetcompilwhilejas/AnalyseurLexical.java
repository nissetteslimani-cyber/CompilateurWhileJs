/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetcompilwhilejas;

 import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnalyseurLexical {
    private String input;
    private int i = 0;
    private int ligne = 1;
    private static final char EOF = '#'; // Le sentinelle

    public AnalyseurLexical(String input) {
        this.input = input + EOF; // Ajout du sentinelle
    }

    private char regarderSuivant() {
        if (i + 1 < input.length()) {
            return input.charAt(i + 1);
        } else {
            return EOF;  
        }
    }

    public List<Token> analyser() {
        List<Token> tokens = new ArrayList<>();

        // 1. La seule vérification de boucle principale doit être contre le sentinelle.
        while (input.charAt(i) != EOF) {

            // Ignorer espaces / tabulations / retour ligne
            // SÉCURITÉ: Vérification d'index obligatoire
            while (i < input.length() && (input.charAt(i) == ' ' || input.charAt(i) == '\n' || input.charAt(i) == '\t')) {
                if (input.charAt(i) == '\n') ligne++;
                i++;
            }

            char tc = input.charAt(i);

            // Si après l'ignorance des espaces on tombe sur le sentinelle, on sort.
            if (tc == EOF) break;

            // ---------------------- COMMENTAIRES (Sécurisé) ----------------------
        
            if (tc == '/') {
                char next = regarderSuivant();
                if (next == '/') { // Commentaire ligne //
                    i += 2; 
                    while (i < input.length() && input.charAt(i) != '\n' && input.charAt(i) != EOF) {
                        i++;
                    }
                    continue;
                }
            }

            // ---------------------- CHAÎNES DE CARACTÈRES (Sécurisé) ----------------------
            if (tc == '"' || tc == '\'') {
                char delimiteur = tc;
                i++;
                String str = "";

                while (i < input.length() && input.charAt(i) != delimiteur && input.charAt(i) != EOF) {
                    if (input.charAt(i) == '\\') {
                        str += input.charAt(i);
                        i++;
                        if (i < input.length()) { // SÉCURITÉ
                            str += input.charAt(i);
                            i++;
                        } else {
                            break; 
                        }
                    } else {
                        str += input.charAt(i);
                        i++;
                    }
                }

                tokens.add(new Token(TypeToken.STRING, str, ligne));

                if (i < input.length() && input.charAt(i) == delimiteur) i++;

                continue;
            }

            // ---------------------- MOTS / IDENTIFIANTS ----------------------
            if ((tc >= 'a' && tc <= 'z') || (tc >= 'A' && tc <= 'Z')) {
                String mot = "" + tc;
                i++;
                // SÉCURITÉ: Vérification d'index obligatoire
                while (i < input.length() && (
                        (input.charAt(i) >= 'a' && input.charAt(i) <= 'z') ||
                        (input.charAt(i) >= 'A' && input.charAt(i) <= 'Z') ||
                        (input.charAt(i) >= '0' && input.charAt(i) <= '9') ||
                        input.charAt(i) == '_')) {
                    mot += input.charAt(i);
                    i++;
                }

                if (mot.equals("while")) tokens.add(new Token(TypeToken.WHILE, mot, ligne));
                else if (mot.equals("if")) tokens.add(new Token(TypeToken.IF, mot, ligne));
                else if (mot.equals("else")) tokens.add(new Token(TypeToken.ELSE, mot, ligne));
                else if (mot.equals("for")) tokens.add(new Token(TypeToken.FOR, mot, ligne));
                else if (mot.equals("do")) tokens.add(new Token(TypeToken.DO, mot, ligne));
                else if (mot.equals("return")) tokens.add(new Token(TypeToken.RETURN, mot, ligne));
                else if (mot.equals("break")) tokens.add(new Token(TypeToken.BREAK, mot, ligne));
                else if (mot.equals("let")) tokens.add(new Token(TypeToken.LET, mot, ligne));
                else if (mot.equals("const")) tokens.add(new Token(TypeToken.CONST, mot, ligne));
                else if (mot.equals("Nissette")) tokens.add(new Token(TypeToken.NISSETTE, mot, ligne));
                else if (mot.equals("Slimani")) tokens.add(new Token(TypeToken.SLIMANI, mot, ligne));
                else if(mot.equals("console.log")) tokens.add(new Token (TypeToken.Console_log,mot,ligne));
                else tokens.add(new Token(TypeToken.VARIABLE, mot, ligne));

                continue;
            }

            // --- OPERATEURS (etc.) ---

            if (tc == '=') {
                if (i + 1 < input.length() && input.charAt(i + 1) == '=') {
                    tokens.add(new Token(TypeToken.EGAL_EGAL, "==", ligne));
                    i += 2;
                } else {
                    tokens.add(new Token(TypeToken.EQUAL, "=", ligne));
                    i++;
                }
                continue;
            }

            if (tc == '<') {
                if (i + 1 < input.length() && input.charAt(i + 1) == '=') {
                    tokens.add(new Token(TypeToken.INFERIEUR_EGALE, "<=", ligne));
                    i += 2;
                } else {
                    tokens.add(new Token(TypeToken.INFERIEUR, "<", ligne));
                    i++;
                }
                continue;
            }

            if (tc == '>') {
                if (i + 1 < input.length() && input.charAt(i + 1) == '=') {
                    tokens.add(new Token(TypeToken.SUPERIEUR_EGALE, ">=", ligne));
                    i += 2;
                } else {
                    tokens.add(new Token(TypeToken.SUPERIEUR, ">", ligne));
                    i++;
                }
                continue;
            }

            if (tc == '!') {
                if (i + 1 < input.length() && input.charAt(i + 1) == '=') {
                    tokens.add(new Token(TypeToken.DIFFERENT, "!=", ligne));
                    i += 2;
                } else {
                    tokens.add(new Token(TypeToken.ERREUR, "!", ligne));
                    i++;
                }
                continue;
            }

            if (tc == '&') {
                if (i + 1 < input.length() && input.charAt(i + 1) == '&') {
                    tokens.add(new Token(TypeToken.ET, "&&", ligne));
                    i += 2;
                } else {
                    tokens.add(new Token(TypeToken.ERREUR, "&", ligne));
                    i++;
                }
                continue;
            }

            if (tc == '|') {
                if (i + 1 < input.length() && input.charAt(i + 1) == '|') {
                    tokens.add(new Token(TypeToken.OU, "||", ligne));
                    i += 2;
                } else {
                    tokens.add(new Token(TypeToken.ERREUR, "|", ligne));
                    i++;
                }
                continue;
            }


            //  OPERATEURS SIMPLES ----------------------
            if (tc == '(') { tokens.add(new Token(TypeToken.PARENTHESE_OUVRANTE, "(", ligne)); i++; continue; }
            if (tc == ')') { tokens.add(new Token(TypeToken.PARENTHESE_FERMANTE, ")", ligne)); i++; continue; }
            if (tc == '{') { tokens.add(new Token(TypeToken.ACCOLADE_OUVRANTE, "{", ligne)); i++; continue; }
            if (tc == '}') { tokens.add(new Token(TypeToken.ACCOLADE_FERMANTE, "}", ligne)); i++; continue; }
            if (tc == '+') { tokens.add(new Token(TypeToken.PLUS, "+", ligne)); i++; continue; }
            if (tc == '-') { tokens.add(new Token(TypeToken.MOINS, "-", ligne)); i++; continue; }
            if (tc == '*') { tokens.add(new Token(TypeToken.FOIS, "*", ligne)); i++; continue; }
            if (tc == '/') { tokens.add(new Token(TypeToken.DIVISE, "/", ligne)); i++; continue; }
            if (tc == '%') { tokens.add(new Token(TypeToken.MODULO, "%", ligne)); i++; continue; }
            if (tc == ';') { tokens.add(new Token(TypeToken.POINT_VERGULE, ";", ligne)); i++; continue; }
            if (tc == '.') { tokens.add(new Token(TypeToken.POINT, ".", ligne)); i++; continue; }

            // NOMBRE ----------------------
            if (tc >= '0' && tc <= '9') {
                String nombre = "" + tc;
                i++;
                // SÉCURITÉ: Vérification d'index obligatoire
                while (i < input.length() && input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                    nombre += input.charAt(i);
                    i++;
                }
                tokens.add(new Token(TypeToken.NOMBRE, nombre, ligne));
                continue;
            }

            // ERREUR ----------------------
            // POINT DE RUPTURE PRÉCÉDENT : tc ne peut pas être EOF.
            if (tc != EOF) {
                tokens.add(new Token(TypeToken.ERREUR, "" + tc, ligne));
                i++;
            }
        } // Fin de la boucle principale

        // ---------------------- EOF ----------------------
        tokens.add(new Token(TypeToken.EOF, "#", ligne));
        System.out.println("Analyse terminée.");
        return tokens;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez votre code :");
        String code = sc.nextLine();
        AnalyseurLexical al = new AnalyseurLexical(code);
        List<Token> tokens = al.analyser();
        for (Token t : tokens) {
            System.out.println(t);
        }
    }
}