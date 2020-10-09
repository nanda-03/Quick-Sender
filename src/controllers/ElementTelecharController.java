/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author ESDRAS
 */
public class ElementTelecharController implements Initializable {

    @FXML
    private Text nomFichier;
    @FXML
    private Text tailleFichier;
    @FXML
    private Text nomExpe;
    private String nomFichiers;
    private String nomExpes;
    private String tailleFichiers;

    public ElementTelecharController(String nomFichier, String tailleFichier, String nomExpe) {
        this.nomFichier=new Text(nomFichier);
        this.tailleFichier=new Text(tailleFichier);
        this.nomExpe=new Text(nomExpe);
        nomFichiers=nomFichier;
        nomExpes=nomExpe;
        tailleFichiers=tailleFichier;
        
    }

    
    public Text getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier.setText(nomFichier);
    }

    public Text getTailleFichier() {
        return tailleFichier;
    }

    public void setTailleFichier(String tailleFichier) {
        this.tailleFichier.setText(tailleFichier);
    }

    public Text getNomExpe() {
        return nomExpe;
    }

    public void setNomExpe(String nomExpe) {
        this.nomExpe.setText(nomExpe);
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.nomFichier.setText(nomFichiers);
        this.nomExpe.setText(nomExpes);
        this.tailleFichier.setText(tailleFichiers);
    }    
    
}
