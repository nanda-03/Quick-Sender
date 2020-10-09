/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;


public class AffTelechargementController implements Initializable {

    
    @FXML
    private Label nom;

    @FXML
    private ProgressBar progress=new ProgressBar();

    @FXML
    private Text percentage;
    private SimpleStringProperty pourcent;


    public Label getNom() {
        return nom;
    }

    public void setNom(Label nom) {
        this.nom = nom;
    }

    public ProgressBar getProgress() {
        return progress;
    }

    public void setProgress(ProgressBar progress) {
        this.progress = progress;
    }

    public Text getPercentage() {
        return percentage;
    }

    public SimpleStringProperty getPourcent() {
        return pourcent;
    }

    public void setPourcent(SimpleStringProperty pourcent) {
        this.pourcent = pourcent;
    }
    
    public void setPercentage(Text percentage) {
        this.percentage = percentage;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
