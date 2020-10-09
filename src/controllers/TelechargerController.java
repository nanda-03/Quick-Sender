/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import principal.Alertes;

/**
 * FXML Controller class
 *
 * @author ESDRAS
 */
public class TelechargerController implements Initializable {
    private ParametresController parControl;
    @FXML
    private AnchorPane telecharger;
    @FXML
    private ListView<HBox> listeTelechargement = new ListView<HBox>();
    @FXML
    private Button ouvrirButton;
    @FXML
    private Button supprimerButton;

    public ListView<HBox> getListeTelechargement() {
        return listeTelechargement;
    }

    public void setListeTelechargement(ListView<HBox> listeTelechargement) {
        this.listeTelechargement = listeTelechargement;
    }
    
    /**
     * Initializes the controller class.
     * @param parControl
     */
    public TelechargerController(ParametresController parControl){
        this.parControl=parControl;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ouvrirButton.setDisable(true);
        supprimerButton.setDisable(true);
        binderTable();
    }    
    private void binderTable(){
        listeTelechargement.getSelectionModel().selectedItemProperty().addListener((obs,ancVal,nouvVal)->{
            if(nouvVal!=null){
                ouvrirButton.setDisable(false);
                supprimerButton.setDisable(false);
                File file=new File(parControl.getRepertoireTelechargement().getText()+"\\"+((Text)(((VBox)(nouvVal.getChildren().get(1))).getChildren().get(0))).getText());
                ouvrirButton.setOnAction((ActionEvent e)->{
                    Desktop desk = Desktop.getDesktop();
                    try {
                        desk.open(file);
                    } catch (IOException ex) {
                        Alertes.alerte("Problème lors de l'ouverture", "Impossible d'ouvrir ce fichier");
                        Logger.getLogger(TelechargerController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                supprimerButton.setOnAction((ActionEvent e)->{
                file.delete();
                listeTelechargement.getItems().remove(nouvVal);
                });
            }
            else{

            }});
    }

}
