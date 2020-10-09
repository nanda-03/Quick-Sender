/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.net.Socket;

/**
 *
 * @author ESDRAS
 */
public class Profil {
    private Socket socket;
    private String nom;

    public Profil(Socket socket, String nom) {
        this.socket = socket;
        this.nom = nom;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDesc(){
        return this.getNom()+"  "+this.getSocket().getInetAddress().getHostAddress();
    }
}
