/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import static javafx.concurrent.Worker.State.CANCELLED;
import static javafx.concurrent.Worker.State.FAILED;
import static javafx.concurrent.Worker.State.SUCCEEDED;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import modele.Client;
import modele.GestionClient;
import modele.Serveur;
import modele.VueConversation;
import principal.Alertes;
import principal.ScanGenerer;

/**
 * FXML Controller class
 *
 * @author ESDRAS
 */
public class ConnexionController implements Initializable {

    @FXML
    private ListView<String> listePersonnes= new ListView<String>();
    @FXML
    private Button scanner;
    private ParametresController parControl;
    @FXML
    private Text adresseIP= new Text();
    @FXML
    private Button boutonConnexion;
    @FXML
    private Text labelDeconnecter;
    private Serveur serveur;
    private GestionClient gestClient;
    private ListView<HBox> listeTelechargement,listeAmis;
    private Service<Void> gestService=null;
    private VueConversation convers;
    Thread t;
    /**
     * Initializes the controller class.
     * @param parControl
     */
    public ConnexionController(ParametresController parControl,ListView<HBox> listeTelechargement,ListView<HBox> listeAmis,VueConversation convers){
        this.parControl=parControl;
        this.listeTelechargement=listeTelechargement;
        this.listeAmis=listeAmis;
        this.convers=convers;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*adresseIP.setText(ScanGenerer.getMonIP());
        t=new Thread(new ScanGenerer(listePersonnes));
        t.start();*/
        adresseIP.setText(ScanGenerer.getMonIP());
        
        scannerReseau();
    }    
    @FXML
    private void scannerReseau() {
        listePersonnes.getItems().clear();
        scanner.setDisable(true);
        adresseIP.setText(ScanGenerer.getMonIP());
        final Service<Void> scanService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() {
                        ScanGenerer scan=new ScanGenerer(listePersonnes);
                        try {
                            scan.chercher();
                        } catch (Exception ex) {
                            Platform.runLater(()->{
                               Alertes.alerte("Erreur", "La machine n'est connectée à aucun reseau"); 
                            });
                            System.out.println(ex);
                        }
                        return null;
                    }
                };
            }
        };
        scanService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) -> {
            switch (newValue) {
                case FAILED:
                case CANCELLED:
                case SUCCEEDED:
                    scanner.setDisable(false);
                    break;
            }
        });
        scanService.restart();
    }
    private void setContenu(){
        GestionClient.getAdresses().clear();
        for(String item: listePersonnes.getItems()){
            System.out.println(item);
            GestionClient.getAdresses().add(item);
        }
    }
        private boolean estAbsentListePersonnes(String ip){
        for(String adresse:listePersonnes.getItems()){
            if(ip.equals(adresse))
                return false;
        }
        return true;
    }

    private boolean estAbsentListeClient(String adresse,ArrayList<Client> listeClients) {
        for(Client client: listeClients){
            if(client.getSocketFile().getInetAddress().getHostAddress().equals(adresse)){
                return false;
            }
        }
        return true;
    }
    @FXML
    private void seConnecter(ActionEvent event) {
        final Service<Void> seConnectService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {                    
                    @Override
                    protected Void call() throws Exception {
                        System.out.println("1");
                        serveur=new Serveur(parControl.getNomUtilisateur().getText(),listeAmis);
                        serveur.acceptDisconnectServices();
                        System.out.println("2");
                        gestClient=new GestionClient(parControl.getNomUtilisateur().getText(),parControl.getRepertoireTelechargement().getText(),listeTelechargement, convers);
                        System.out.println("3");
                        gestClient.getCheminReper().bind(parControl.getRepertoireTelechargement().textProperty());    
                        System.out.println("4");
                        for(String item: listePersonnes.getItems()){
                            gestClient.getAdresses().add(item);
                            System.out.println("Le client est ajouté dans gestionClient");
                        }
                        for(String i: gestClient.getAdresses()){
                            System.out.println("element: "+i);
                        }
                        return null;
                    }
                };
            }
        };
        seConnectService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) -> {
            switch (newValue) {
                case FAILED:
                    Alertes.alerte("Problème de connexion", "Impossible de se connecter au réseau");
                    break;
                case CANCELLED:
                case SUCCEEDED:
                    Alert alerte = new Alert(AlertType.INFORMATION);
                    alerte.setTitle("Connexion établie");
                    alerte.setHeaderText(null);
                    alerte.setContentText("Vous êtes maintenant connectés au réseau");
                    alerte.show();
                        gestService = new Service<Void>() {
                        @Override
                        protected Task<Void> createTask() {
                            return new Task<Void>() {
                                @Override
                                    protected Void call() throws Exception {
                                        System.out.println("e service d ajout est lançé");
                                        t=new Thread(gestClient);
                                        t.start();
                                        return null;
                                    }
                                };
                            }
                        };
                    gestService.restart();
                    break;
            }
        });
        seConnectService.restart();
        boutonConnexion.setDisable(true);
        labelDeconnecter.setDisable(false);
    }
    @FXML
    private void seDeconnecter(MouseEvent event) {
        try {
            if(gestClient!=null && gestClient.getListeClients()!=null){
                if(t.isAlive())
                    t.interrupt();
            }
            serveur.fermer();
            boutonConnexion.setDisable(false);
            labelDeconnecter.setDisable(true);
            if(gestClient!=null)
                gestClient.getListeClients().clear();
            

        } catch (IOException ex) {
            Alertes.alerte("Problème de déconnexion", "Impossible de se déconnecter");
            Logger.getLogger(ConnexionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
