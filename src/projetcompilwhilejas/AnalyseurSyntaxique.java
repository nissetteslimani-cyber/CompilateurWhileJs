/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetcompilwhilejas;

/**
 *
 * @author USER
 */
  import java.util.List;

 import java.util.List;
import java.util.ArrayList;

   
  

public class AnalyseurSyntaxique {

    private List<Token> tokens;
    private int i = 0;
    private TypeToken token;
    private List<String> erreurs = new ArrayList<>();

    public AnalyseurSyntaxique(List<Token> tokens) {
        this.tokens = tokens;
      
        if (!tokens.isEmpty()) {
            this.token = tokens.get(0).type; 
        } else {
            this.token = TypeToken.EOF;
        }
    }

    private void erreur(String msg) {
    
        if (i < tokens.size()) {
             erreurs.add(" Erreur syntaxique: " + msg + " (jeton: " + tokens.get(i).valeur + ") ligne " + tokens.get(i).ligne);
        } else {
             erreurs.add("Erreur syntaxique: " + msg + " (Fin de fichier inattendue)");
        }
    }
    
  
     private void consume(TypeToken expected, String message) {
    if (token != expected) {
        erreur(message);
    } else {
        suivant();
    }
}


    private void suivant() {
        i++;
        if (i < tokens.size()) token = tokens.get(i).type;
        else token = TypeToken.EOF;
    }

    // program → instruction program | ε
    public void program() {
        while (token == TypeToken.WHILE || token == TypeToken.LET || token == TypeToken.CONST ||
               token == TypeToken.VARIABLE || token == TypeToken.RETURN ||
               token == TypeToken.BREAK || token == TypeToken.SLIMANI || token == TypeToken.NISSETTE) {
            instruction();
        }
    }
private void skipInstruction() {

    // sauter le mot-clé ignoré
    suivant();

    // si on a une parenthèse, on saute le contenu (if(x==3), for(..), etc)
    if (token == TypeToken.PARENTHESE_OUVRANTE) {

        int depthPar = 1;
        suivant();

        while (i < tokens.size() && depthPar > 0) {
            if (token == TypeToken.PARENTHESE_OUVRANTE) depthPar++;
            if (token == TypeToken.PARENTHESE_FERMANTE) depthPar--;
            suivant();
        }
    }

    // maintenant, s’il y a un bloc {...}
    if (token == TypeToken.ACCOLADE_OUVRANTE) {

        int depthBloc = 1;
        suivant();

        while (i < tokens.size() && depthBloc > 0) {
            if (token == TypeToken.ACCOLADE_OUVRANTE) depthBloc++;
            if (token == TypeToken.ACCOLADE_FERMANTE) depthBloc--;
            suivant();
        }

        return;
    }

    // sinon on skip jusqu’au ;
    while (token != TypeToken.POINT_VERGULE && token != TypeToken.EOF) {
        suivant();
    }

    if (token == TypeToken.POINT_VERGULE) suivant();
}

    private boolean isIgnoredKeyword(TypeToken t) {
    return t == TypeToken.IF ||
           t == TypeToken.ELSE ||
           t == TypeToken.CATCH ||
           t == TypeToken.CASE ||
           t == TypeToken.DO||
           t == TypeToken.Console_log ||
           t == TypeToken.FOR ||
           t == TypeToken.TRY;
}

    // instruction → while_statement | declaration | assignation | returnStatement | breakStatement | slimani | nissette
     private void instruction() {

    if (isIgnoredKeyword(token)) {
        skipInstruction();
        return;
    }

    switch (token) {
        case LET:
        case CONST: declaration(); break;

        case VARIABLE: assignation(); break;

        case WHILE: whileInstr(); break;

        case RETURN: returnInstr(); break;

        default:
            erreur("Instruction invalide : " + token);
    }
}


    // while_statement → while ( condition ) { program }
   private void whileInstr() {
    
    if (token != TypeToken.WHILE) erreur("WHILE attendu");
    else suivant();

    if (token != TypeToken.PARENTHESE_OUVRANTE) erreur("( attendue après while");
    else suivant();

    condition();

    if (token != TypeToken.PARENTHESE_FERMANTE) erreur(") attendue après condition");
    else suivant();

    if (token != TypeToken.ACCOLADE_OUVRANTE) erreur("{ attendue pour le bloc while");
    else suivant();

    program();

    if (token != TypeToken.ACCOLADE_FERMANTE) erreur("} attendue pour le bloc while");
    else suivant();
}


    // declaration → (let|const) variable = expression ;
    private void declaration() {
    if (token != TypeToken.LET && token != TypeToken.CONST) {
        erreur("let ou const attendu");
    } else suivant();

    if (token != TypeToken.VARIABLE) erreur("nom de variable attendu");
    else suivant();

    if (token != TypeToken.EQUAL) erreur("= attendu après variable");
    else suivant();

    expression();

    if (token != TypeToken.POINT_VERGULE) erreur("; attendu après expression");
    else suivant();
}


    // assignation → variable = expression ;
    
private void assignation() {
    if (token != TypeToken.VARIABLE) erreur("variable attendue");
    else suivant();

    if (token != TypeToken.EQUAL) erreur("= attendu pour l'assignation");
    else suivant();

    expression();

    if (token != TypeToken.POINT_VERGULE) erreur("; attendu après expression");
    else suivant();
}

    // returnStatement → return expression ;
    private void returnInstr() {
    if (token != TypeToken.RETURN) erreur("return attendu");
    else suivant();

    expression();

    if (token != TypeToken.POINT_VERGULE) erreur("; attendu après return");
    else suivant();
}


    
     private void breakInstr() {
    if (token != TypeToken.BREAK) erreur("break attendu");
    else suivant();

    if (token != TypeToken.POINT_VERGULE) erreur("; attendu après break");
    else suivant();
}

private void slimaniInstr() {
    if (token != TypeToken.SLIMANI) erreur("Slimani attendu");
    else suivant();

    if (token != TypeToken.POINT_VERGULE) erreur("; attendu après Slimani");
    else suivant();
}

private void nissetteInstr() {
    if (token != TypeToken.NISSETTE) erreur("Nissette attendu");
    else suivant();

    if (token != TypeToken.POINT_VERGULE) erreur("; attendu après Nissette");
    else suivant();
}


    // condition → expression operation expression
    private void condition() {
        expression();
        operation();
        expression();
    }

    // operation → > | < | >= | <= | == | !=
    private void operation() {
        if (token == TypeToken.SUPERIEUR || token == TypeToken.INFERIEUR ||
            token == TypeToken.SUPERIEUR_EGALE || token == TypeToken.INFERIEUR_EGALE ||
            token == TypeToken.EGAL_EGAL || token == TypeToken.DIFFERENT) {
            suivant();
        } else {
            erreur("Opérateur relationnel attendu");
        }
    }

    // expression → term { (+|-) term }
    private void expression() {
        term();
        while (token == TypeToken.PLUS || token == TypeToken.MOINS) {
            suivant();
            term();
        }
    }

    // term → facteur { (*|/|%) facteur }
    private void term() {
        facteur();
        while (token == TypeToken.FOIS || token == TypeToken.DIVISE || token == TypeToken.MODULO) {
            suivant();
            facteur();
        }
    }

   // facteur → nombre | variable | ( expression )
private void facteur() {

    if (token == TypeToken.NOMBRE || token == TypeToken.VARIABLE) {
        suivant();
        return;
    }

    if (token == TypeToken.PARENTHESE_OUVRANTE) {
        suivant(); 

        expression();

        if (token != TypeToken.PARENTHESE_FERMANTE)
            erreur(") attendue");
        else
            suivant();  
        
        return;
    }

    // Sinon erreur
    erreur("Facteur invalide (nombre, variable ou '(' attendu)");
    suivant();  
}

     
    public void Z() {
        i = 0;
        if (!tokens.isEmpty()) {
            token = tokens.get(0).type;
        } else {
            token = TypeToken.EOF;
        }
        
        erreurs.clear();

        program(); 

        // Affichage final
        if (erreurs.isEmpty() && token == TypeToken.EOF) {
            System.out.println(" Chaîne acceptée");
        } else {
            System.out.println(" Chaîne refusée :");
            for (String erreur : erreurs) {
                System.out.println(erreur);
            }
            if (token != TypeToken.EOF) {
                 System.out.println("️ Analyse arrêtée prématurément. Jetons restants après analyse complète (dernier jeton vu: " + tokens.get(i).valeur + ").");
            }
        }
    }
}