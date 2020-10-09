/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author ESDRAS
 */
public class EmissionFichier implements Constantes{
    private Socket socket ;
    private OutputStream out2 ;
    private RandomAccessFile outFile;
    private PrintWriter out;
    private String chemin;
    private String nomExpe;
    private DoubleProperty taille=new SimpleDoubleProperty();
    public EmissionFichier(Socket socket,String nomExpe,String chemin){
        this.socket=socket;
        this.nomExpe=nomExpe;
        this.chemin=chemin;
        this.taille.set(0);
    }

    public EmissionFichier(Socket socket,String nomExpe){
        this.socket=socket;
        this.nomExpe=nomExpe;
        this.taille.set(0);
    }    
    public void envoyerMessage(String message) throws IOException{
        out=new PrintWriter(socket.getOutputStream());
        System.out.println("Message: "+message);
        out.println("&&");
        out.println(nomExpe+ ":");
        out.println(message);
        out.println("***");
        out.flush();
        //out.close();
    }
    public boolean envoyer(){
        try {
            System.out.println("L'envoi commence");
            caracFichier();
            envoyerNom();
            envoyerFichier();
            System.out.println("éfin");
            return true;
        } catch (IOException ex) {
            Logger.getLogger(EmissionFichier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public boolean envoyerFichier(){
        try{
            Socket t = new Socket(socket.getInetAddress().getHostAddress(),PORT); //nouvelle socket pour le transfert du fichier  
             // création du fichier en lecture  
            out2 = t.getOutputStream(); 
            byte data[] = new byte[TAILLEFLUX];
            int amount;
            double tailleF =outFile.length();
            double a=0;
            while((amount = outFile.read(data)) != -1){
                System.out.println(amount);
                a+=amount;
                taille.set(a/tailleF);
                out2.write(data, 0,amount);
            }
            out2.flush();
            out2.close();
            System.out.println("Envoyé "+tailleF);
            outFile.close();
            t.close();
            return true;
   }catch(IOException e){

   }
        return false;

    }  
    public void caracFichier(){
        try {
            outFile = new RandomAccessFile(chemin,"r" );
        } catch (FileNotFoundException ex) {
            System.out.println("Probleme de lecture du fichier");
            Logger.getLogger(EmissionFichier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void envoyerNom() throws IOException{
        out=new PrintWriter(socket.getOutputStream());
        out.println(uniformiserChaine(nettoyerChaine()));
        out.flush();
        //out.close();
    }
    public String nettoyerChaine(){
        if(chemin.length()!=0){
            int i=chemin.length()-1;
            while(i>0){
                if(chemin.charAt(i)=='\\' && i<chemin.length()-1){
                    return chemin.substring(i+1);
                }
                i--;
            }
            return chemin;
        }
        return chemin;
    }
    public String uniformiserChaine(String s){
        try {
            long taille= outFile.length();
            long tailleF=(long)taille/1024+ 1;
            return s+"?"+nomExpe+"*"+tailleF;
        } catch (IOException ex) {
            Logger.getLogger(EmissionFichier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getNomExpe() {
        return nomExpe;
    }

    public void setNomExpe(String nomExpe) {
        this.nomExpe = nomExpe;
    }

 public DoubleProperty getTailleProperty() {
        return taille;
    }

    public void setTailleProperty(DoubleProperty taille) {
        this.taille = taille;
    }

    public RandomAccessFile getOutFile() {
        return outFile;
    }
    
    
}
