/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import principal.Alertes;
import principal.Transfer;
import modele.Connexion;
import modele.EmissionFichier;
import modele.Profil;
import principal.Progression;

/**
 * FXML Controller class
 *
 * @author ESDRAS
 */
public class EnvoyerController implements Initializable {
    private ParametresController parControl;
    @FXML
    private AnchorPane envoyer;
    @FXML
    private Button ajouterFichier;
    @FXML
    private Button envoyerFichier;
    @FXML
    private Button envoyerTout;
    @FXML
    private Button viderListe;
    @FXML
    private ListView<HBox> listeEnvoi;
    @FXML
    private Button retirer;
    private String image="..\\image\\File.png";
    public EnvoyerController(ParametresController parControl){
        this.parControl=parControl;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        envoyerTout.setDisable(true);
        envoyerFichier.setDisable(true);
        binderTable();
    }    

    @FXML
    private void ajouterFichier(ActionEvent event) {
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Selectionner le fichier");
        File homeDir = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(homeDir);
        
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("All Files","*.*"));
        File selectedFile=fileChooser.showOpenDialog(Transfer.primaryStage);
        if (selectedFile != null) {
            try {
                HBox hbox=new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.getChildren().add(new ImageView("/image/File.png"));
                Text text=new Text(selectedFile.getAbsolutePath());
                hbox.getChildren().add(text);
                listeEnvoi.getItems().add(hbox);
            }
            catch (Exception e) {
                Alertes.alerte("Problème d'ajout du fichier","Impossible d'ajouter le fichier veuillez réessayer");
            }
        }
    }

    @FXML
    private void viderListe(ActionEvent event) {
        listeEnvoi.getItems().clear();
    }
    private void binderTable(){
        listeEnvoi.getSelectionModel().selectedItemProperty().addListener((obs,ancVal,nouvVal)->{
            if(nouvVal!=null){
                envoyerFichier.setDisable(false);
                envoyerTout.setDisable(false);
                envoyerFichier.setOnAction((ActionEvent e)->{
                    if(!Connexion.addr.isEmpty()){
                        ArrayList<String> adresses= Connexion.adresses();
                        ChoiceDialog<String> cDial = new ChoiceDialog<>(adresses.get(0), adresses);
                        cDial.setTitle("Choix du destinataire");
                        cDial.setContentText("Destinataire:");
                        Optional<String> selection = cDial.showAndWait();
                        selection.ifPresent(str ->{ 
                            Profil profil=Connexion.estEgal(cDial.getSelectedItem());
                            if(profil.getSocket()!=null)
                                envoyerService(profil,nouvVal);
                        });
                    }
                    else{
                        Alertes.alerte("Impossible de choisir un destinataire", "Vous n'êtes connectés à personne");
                    }
                });
                retirer.setOnAction((ActionEvent e)->{
                    listeEnvoi.getItems().remove(nouvVal);
                });
                envoyerTout.setOnAction((ActionEvent e)->{
                    if(!Connexion.addr.isEmpty()){
                        for(Profil s: Connexion.addr){
                            envoyerService(s,nouvVal);   
                        }    
                    }
                    else{
                        Alertes.alerte("Impossible de choisir un destinataire", "Vous n'êtes connectés à personne");
                    }
                });
            }
        });
    }
    public void envoyerService(Profil s,HBox nouvVal){
        EmissionFichier emiFichier=new EmissionFichier(s.getSocket(),parControl.getNomUtilisateur().getText(),((Text)((HBox)nouvVal).getChildren().get(1)).getText());
        parControl.getNomUtilisateur().textProperty().addListener((ob, anc,nouv)->{
            if(nouv!=null){
                emiFichier.setNomExpe(nouv);
            }
        });
        final Service<Void> envoiService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        System.out.println(((Text)((HBox)nouvVal).getChildren().get(1)).getText());
                        Platform.runLater(()->{
                        Progression.genererProgression("Envoi du fichier ",nettoyerChaine(s.getNom()),
                                ((Text)((HBox)nouvVal).getChildren().get(1)).getText(), emiFichier.getTailleProperty());
                        });
                        System.out.println("impossible de commencer l'envoi");
                        boolean bool=emiFichier.envoyer();
                        System.out.println("impossible de commencer l'envoi");
                        if(bool){
                            Platform.runLater(()->{
                                Alert alerte = new Alert(AlertType.INFORMATION);
                                alerte.setTitle("Envoi du fichier à "+s.getSocket().getInetAddress().getHostAddress());
                                alerte.setHeaderText(null);
                                alerte.setContentText("Envoi terminé");
                                alerte.showAndWait();
                            });
                            
                        }
                        else{
                            Alertes.alerte("Problème lors de l'envoi","Impossible d'envoyer ce fichier");
                        }
                    return null;
                }
                };
            }
        };
        envoiService.restart();
    }
        public String nettoyerChaine(String chemin){
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
}
