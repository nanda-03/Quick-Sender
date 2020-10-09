/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import modele.LectureFichier;
import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import principal.Alertes;
import principal.Transfer;

/**
 * FXML Controller class
 *
 * @author ESDRAS
 */
public class ParametresController implements Initializable {

    @FXML
    private TextField nomUtilisateur=new TextField();
    @FXML
    private Text repertoireTelechargement=new Text();
    @FXML
    private Button changerRepertoire;
    @FXML
    private Button terminer;

    public TextField getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(TextField nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public Text getRepertoireTelechargement() {
        return repertoireTelechargement;
    }

    public void setRepertoireTelechargement(Text repertoireTelechargement) {
        this.repertoireTelechargement = repertoireTelechargement;
    }


    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] tab=LectureFichier.retournerValeur();
        System.out.println(tab[0]+"     "+tab[1]);
        if(!(tab==null)){
            if(tab[0].length()!=0){
                nomUtilisateur.setText(tab[0]);
            }
            if(tab[1].length()!=0){
                repertoireTelechargement.setText(tab[1]);
            }
        }
    }    

    @FXML
    private void changerRepertoire(ActionEvent event) {
        DirectoryChooser dirChooser= new DirectoryChooser();
        dirChooser.setTitle("Selectionner le repertoire");
        File selectedFile = dirChooser.showDialog(Transfer.primaryStage);
        if (selectedFile != null) {
            try {
                repertoireTelechargement.setText(selectedFile.getAbsolutePath());
            }
            catch (Exception e) {
                 Alertes.alerte("Problème de sélection du repertoire","Impossible de choisir le repertoire veuillez réessayer");
            }
        }
    }

    @FXML
    private void changerParametres(ActionEvent event) {
        int i=LectureFichier.ecrire(nomUtilisateur.getText(),repertoireTelechargement.getText());
        if(i==-1){
            Alertes.alerte("Problème d'identification","Erreur lors de la sauvegarde de vos données");
        }
        else{
            Alertes.alerte("Modifications effectées","Vos modifications ont été effectuées");
        }
    }

}
