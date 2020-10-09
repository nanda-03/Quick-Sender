/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import principal.Alertes;

/**
 *
 * @author ESDRAS
 */
public class GestionClient extends Utilisateur implements Runnable{
    private static ArrayList<Client> listeClients=new ArrayList<Client>();
    private static ArrayList<String> adresses=new ArrayList<String>();
    private StringProperty cheminReper=new SimpleStringProperty();
    private ListView<HBox> listeTelechargement;
    private VueConversation convers;
    public GestionClient(String s,String chem,ListView<HBox> listeTelechargement, VueConversation convers) {
        super(s);
        cheminReper.setValue(chem);
        this.listeTelechargement=listeTelechargement;
        this.convers=convers;
    }
    
    public void retirerClient() throws IOException{
        for(Client client: listeClients){
            if(estAbsentListeAdresse(client.getSocketFile().getInetAddress().getHostAddress())){
                listeClients.remove(client);
            }
        }
    }

    public StringProperty getCheminReper() {
        return cheminReper;
    }

    public void setCheminReper(StringProperty cheminReper) {
        this.cheminReper = cheminReper;
    }
    
    public void ajouterClient(){
        Client client;
        for(String adresse: GestionClient.adresses){
            if(estAbsentListeClient(adresse)){
                try {
                        client=new Client(nom, adresse,cheminReper,listeTelechargement,convers);
                        if(client.getSocketFile()!=null){
                            client.lancer(); 
                            listeClients.add(client);
                            System.out.println("ajouterclients est effectué");
                        }
                } catch (IOException ex) {
                    Logger.getLogger(GestionClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    private boolean estAbsentListeAdresse(String ip){
        for(String adresse:adresses){
            if(ip.equals(adresse))
                return false;
        }
        return true;
    }

    private boolean estAbsentListeClient(String adresse) {
        for(Client client: listeClients){
            if(client.getSocketFile().getInetAddress().getHostAddress().equals(adresse)){
                return false;
            }
        }
        return true;
    }

    public static ReceptionFichier estEgal(String s){
        for(Client c: listeClients){
            if(c.getAdresse().equals(s))
                return c.getRecepFile();
        }
        return null;
    }
    public ArrayList<Client> getListeClients() {
        return listeClients;
    }

    public static ArrayList<String> getAdresses() {
        return adresses;
    }

    public static void setAdresses(ArrayList<String> adresses) {
        GestionClient.adresses = adresses;
    }
    public synchronized void uniformiserChemin(){
        for(Client c: listeClients){
            c.setCheminReper(cheminReper);
            c.getRecepFile().setCheminReper(cheminReper);
        }
    }
    
    public void lancer() {
    try {
            retirerClient();
            ajouterClient();
            cheminReper.addListener((obs,ancVal,nouvVal)->{
                uniformiserChemin();
            });
        } catch (IOException ex) {
            Logger.getLogger(GestionClient.class.getName()).log(Level.SEVERE, null, ex);
            Alertes.alerte("Problème de gestion","Impossible d'ajouter ou de retirer des clients");
        }
    }

    @Override
    public void run() {
        while(true)
            lancer();
    }
}
