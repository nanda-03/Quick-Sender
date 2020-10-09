/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.net.Socket;
import java.util.ArrayList;

public class Connexion {

    private Socket socket=null;
    boolean connect=false;
    public static ArrayList<Profil> addr =new ArrayList<Profil>();

    public Connexion(Socket s) {
        socket=s;
    }
    public boolean isConnect() {
        return connect;
    }
    public void setConnect(boolean connect) {
        this.connect = connect;
    }
    public static ArrayList<String> adresses(){
        ArrayList<String> adresses=new ArrayList<String>();
        for(Profil s: addr){
            adresses.add(s.getDesc());
        }
        return adresses;
    }
    public static Profil estEgal(String s){
        for(Profil p: addr){
            if(p.getDesc().equals(s)){
                return p;
            }
        }
        return null;
    }
    public static Profil estEgal(Socket s){
        for(Profil p: addr){
            if(p.getSocket().getInetAddress().getHostAddress().equals(s.getInetAddress().getHostAddress())){
                return p;
            }
        }
        return null;
    }
}
