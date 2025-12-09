/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetcompilwhilejas;

/**
 *
 * @author USER
 */
 public class Token {
    public TypeToken type;
     String valeur;
    int ligne;
    
    public Token(TypeToken type, String valeur, int ligne){
   this.type=type;
   this.valeur=valeur;
   this.ligne=ligne;
    
}
    @Override
    public String toString() {
        return "Token [ type: " + type +", val: \"" + valeur + "\", ligne:" + ligne + " ]";
    }
    
}
