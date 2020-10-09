/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import controllers.ElementTelecharController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import principal.Alertes;
import principal.Progression;

/**
 *
 * @author ESDRAS
 */
public class ReceptionFichier implements Constantes,Runnable{
    private String nomExpe;
    private Socket socket ;
    private BufferedReader in;
    public String nomFichier;
    public String tailleFichier;
    public DoubleProperty progress =new SimpleDoubleProperty(0);
    public StringProperty cheminReper=new SimpleStringProperty();
    ListView<HBox> listeTelechargement;
    private VueConversation convers;
//partie qu'on peut delete
    public ReceptionFichier(Socket socket, String s) {
        this.socket = socket;
        cheminReper.setValue(s);
    }
    public void lancer2(){
        if(traiterChaine())
            if(recevoirFichier()){
                System.out.println("Reussi");
            }
    }
//fin de la partie

    /**
     *
     * @param socket
     * @param s
     * @param listeTelechargement
     */
    public ReceptionFichier(Socket socket,String s,ListView<HBox> listeTelechargement, VueConversation convers) {
        this.socket = socket;
        cheminReper.setValue(s);
        this.listeTelechargement=listeTelechargement;
        this.convers=convers;
    }

    public ReceptionFichier(Socket s,ReceptionFichier recep){
        this.socket=s;
        cheminReper=recep.getCheminReper();
        this.listeTelechargement=recep.listeTelechargement;
        this.tailleFichier=recep.tailleFichier;
        this.nomFichier=recep.nomFichier;
        this.nomExpe=recep.nomExpe;        
    }
    public boolean traiterChaine(){
        try{
           in=new BufferedReader(new InputStreamReader (socket.getInputStream()));
           String s=in.readLine();
           if(s!=null){
               if (s.equals("&&")){
                   String msg="";
                   while(!(s=in.readLine()).equals("***")){
                       msg+=s+'\n';
                       System.out.println(s);
                   }
                   System.out.print(msg);
                   convers.receiveMessage(msg.substring(0,msg.length()-1));
                   Platform.runLater(()->{
                       Alertes.notification("Message reçu", "Vous avez reçu un nouveau message");
                   });
               }
               else{
                    int i=0,p=0;
                     while(i<s.length()){
                         if(s.charAt(i)=='?' && i>0){
                             nomFichier=s.substring(0,i);
                             p=i;
                         }
                         if((s.charAt(i))=='*'){
                             nomExpe=s.substring(p+1,i);
                             if(i<s.length()-1){
                                 tailleFichier=s.substring(i+1)+" KB";
                                 System.out.println("Nom Expe: "+nomExpe+" Nom Fichier: "+nomFichier+" taille Fichier: "+tailleFichier);
                                  return true;
                             }
                         }
                         i++;
                     }
                         return false;
                    }
                    return false;
                }
            
        } catch (IOException ex) {
            //Logger.getLogger(ReceptionFichier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean recevoirFichier(){
        try{
            System.out.println("reception commençée");
            Socket s= socket;
            String chem=cheminReper.getValue();
            OutputStream inFile=new FileOutputStream(new File(chem+"\\"+nomFichier));
            //RandomAccessFile inFile = new RandomAccessFile ( ); 
            InputStream in2 = socket.getInputStream(); 
            byte data[] = new byte[TAILLEFLUX];
            int amount;
            double a=0,ta=tailleFichier();
            Platform.runLater(()->{
                Progression.genererProgression("Réception du fichier", nomExpe,nomFichier, progress);
            });
            while((amount = in2.read(data)) != -1){
                inFile.write(data,0,amount);
                a+=amount;
                progress.setValue(a/ta);
                System.out.println("en cours");
            }
            System.out.println("fin");
            in2.close();
            inFile.close();
            s.close();
            Platform.runLater(()->{
                try {
                FXMLLoader loader= new FXMLLoader();
                loader.setControllerFactory(c->{return new ElementTelecharController(nomFichier,tailleFichier,nomExpe);});                  
                loader.setLocation(getClass().getResource("/vues/elementTelechar.fxml"));
                System.out.println(loader.getLocation().toString()); 
                listeTelechargement.getItems().add((HBox)loader.load());
                Alertes.notification("Fichier reçu","Reception terminée");
            } catch (IOException ex) {
                Logger.getLogger(ReceptionFichier.class.getName()).log(Level.SEVERE, null, ex);
                Platform.runLater(()->{
                Alertes.alerte("Problème de réception de fichiers","Un fichier a été reçu mais n'a pas été ajouté à la liste. Bien vouloir consulter votre repertoire de telechargement");
            });
            }
            });
            return true;
        }
        catch(IOException e){
            System.out.println("sdfghhfghfghfghfghfghfghf");
            System.out.println(e);} 
        return false;
    }

    public void lancer(){
        if(!(socket.isClosed())){
            if(traiterChaine()){
                Platform.runLater(()->{
                    Progression.genererProgression("Réception du fichier", nomExpe,nomFichier, progress);
                });
                if(recevoirFichier()){
                    try {
                        FXMLLoader loader= new FXMLLoader();

                        loader.setControllerFactory(c->{return new ElementTelecharController(nomFichier,tailleFichier,nomExpe);});                  
                        loader.setLocation(getClass().getResource("/vues/elementTelechar.fxml"));
                        System.out.println(loader.getLocation().toString()); 
                        listeTelechargement.getItems().add((HBox)loader.load());
                        Platform.runLater(()->{
                                Alertes.notification("Fichier reçu","Reception terminée");
                    });
                    } catch (IOException ex) {
                        Logger.getLogger(ReceptionFichier.class.getName()).log(Level.SEVERE, null, ex);
                        Platform.runLater(()->{
                        Alertes.alerte("Problème de réception de fichiers","Un fichier a été reçu mais n'a pas été ajouté à la liste. Bien vouloir consulter votre repertoire de telechargement");
                    });
                    }
                }
            }
        }
    }
    public long tailleFichier(){
        long ta=Long.parseLong(tailleFichier.substring(0,tailleFichier.length()-3));
        return (ta-1)*1024;
    }
    public String getNomExpe() {
        return nomExpe;
    }

    
    public void setNomExpe(String nomExpe) {
        this.nomExpe = nomExpe;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public StringProperty getCheminReper() {
        return cheminReper;
    }

    public void setCheminReper(StringProperty cheminReper) {
        this.cheminReper = cheminReper;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    @Override
    public void run() {
        while(true){
            traiterChaine();
        }
    }
    
}
