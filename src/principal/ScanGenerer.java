/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

/**
 *
 * @author ESDRAS
 */
public class ScanGenerer{
    private ListView<String> liste;
    public ScanGenerer(ListView<String> liste){
        this.liste=liste;
    }
    public static String getMonIP(){
        try { 
            String myip=InetAddress.getLocalHost().getHostAddress();
            return myip;
        } catch (UnknownHostException ex) {Alertes.alerte("Erreur", "Impossible de trouver l'ip de la machine");}
        return "Inconnu";
    }

    public void chercher() throws Exception {
        if(!ScanGenerer.getMonIP().equals("127.0.0.1")){
            ExecutorService pool = Executors.newCachedThreadPool();
            for(int i = 0; i < 250; i+=10) {
                pool.execute(new Scan(i,i+10));
            }
            pool.execute(new Scan(250,255));
            pool.shutdown();
            try {
                pool.awaitTermination(20, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(ScanGenerer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            throw new Exception("Pas connecté");
        }
    }
    private class Scan implements Runnable{
        int p;
        int j;
        public Scan(int p, int j){
            this.p=p;
            this.j=j;
        }
        private synchronized void scanReseau(){
            try {
                String myip=InetAddress.getLocalHost().getHostAddress(); // retourne mon adresse IP
                if(myip.equals("127.0.0.1")){
                    System.out.println("Pas connecté");
                }
                else {
                    String mynetworkips=new String();
                    // son role est de trouver l'adresse de sous reseau dans laquelle je suis
                    for(int i=myip.length()-1;i>=0;--i){
                        if(myip.charAt(i)=='.'){
                            mynetworkips=myip.substring(0,i+1);
                            break;
                        }
                    }
                    System.out.println("Mon adresse IP: " + myip+"\n"); 
                    System.out.println("Recherche: ");
                    for(int i=p;i<j;++i){
                        try {
              
                            InetAddress addr=InetAddress.getByName(mynetworkips + new Integer(i).toString());
                            
                            if (addr.isReachable(1000)){ 
                                System.out.println("Available: " + addr.getHostAddress());
                                if(!addr.getHostAddress().equals(myip))
                                    ajouter(addr.getHostAddress());
                            }
                        }catch (IOException ioex){
                            Platform.runLater(()->{Alertes.alerte("Erreur", "Erreur de connexion");});
                        }
                    }
                }
            } catch (UnknownHostException ex) {
                 Platform.runLater(()->{Alertes.alerte("Erreur", "Erreur de connexion");});
            }
        }

    @Override
    public void run() {
        scanReseau();
    }
   
    }
    public synchronized void ajouter(String machDispo){
        Platform.runLater(()->{
            if(!(liste.getItems().contains(machDispo)))
            liste.getItems().add(machDispo);
        });
    }
    
}
