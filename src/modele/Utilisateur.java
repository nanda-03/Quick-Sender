/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;



public class Utilisateur {
    protected String nom;
    public final int PORT=21;
    //CONSTRUCTOR
    public Utilisateur(String s){
        nom=s;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
}
