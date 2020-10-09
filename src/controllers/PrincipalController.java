/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.AmisController;
import controllers.ConnexionController;
import controllers.EnvoyerController;
import controllers.ParametresController;
import controllers.TelechargerController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import principal.Alertes;
import principal.Alertes;

/**
 *
 * @author ESDRAS
 */
public class PrincipalController implements Initializable {
    
    @FXML
    private Label label=new Label("Bouton");
    @FXML
    private AnchorPane connexion;
    @FXML
    private AnchorPane telechargement;
    @FXML
    private AnchorPane envoi;
    @FXML
    private AnchorPane parametre;
    @FXML
    private AnchorPane espace;
    protected FXMLLoader loaderAccueil=new FXMLLoader();
    protected FXMLLoader loaderTelechar=new FXMLLoader();
    protected FXMLLoader loaderEnvoyer=new FXMLLoader();
    protected FXMLLoader loaderParam=new FXMLLoader();
    protected FXMLLoader loaderAmis=new FXMLLoader();
    protected ConnexionController conControl;
    protected EnvoyerController envControl;
    protected ParametresController parControl;
    protected TelechargerController telControl;
    protected AmisController amisControl;
    protected AnchorPane accueil;
    protected AnchorPane telechar;
    protected AnchorPane envoyer;
    protected AnchorPane param;
    protected AnchorPane listeAmis;
    protected String couleur1="#333333";
    protected String couleur2="#b0f";
    @FXML
    private AnchorPane amis;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        param=chargerVue("/vues/Parametres.fxml",loaderParam);
        parControl=loaderParam.getController();
        listeAmis=chargerVue("/vues/Amis.fxml",loaderAmis);
        amisControl=loaderAmis.getController();
        telechar=chargerVue("/vues/Telecharger.fxml",loaderTelechar);
        telControl=loaderTelechar.getController();
        accueil=chargerVue("/vues/Connexion.fxml",loaderAccueil);
        conControl=loaderAccueil.getController();
        envoyer=chargerVue("/vues/Envoyer.fxml",loaderEnvoyer);
        envControl=loaderEnvoyer.getController();
        label.setText("Parametres");
        espace.getChildren().clear();
        if(param!=null){
            espace.getChildren().add(param);
            changerCouleur(parametre,couleur2);
            
        }
    }    

    @FXML
    private void choisirVue(MouseEvent event) {
        AnchorPane ob= (AnchorPane)event.getSource();
        resetCouleur();
        if(ob.equals(connexion)){
            try{
                label.setText("Accueil");
                espace.getChildren().clear();
                changerCouleur(connexion,couleur2);
                if(accueil!=null)
                    espace.getChildren().add(accueil);
            }catch(Exception e){
                Alertes.alerte("Erreur lors de l'ouverture", "Veuillez reessayer");
            }
        }
        else{
            if(ob.equals(telechargement)){
                try{
                    label.setText("Telechargement");
                    espace.getChildren().clear();
                    changerCouleur(telechargement,couleur2);
                    if(telechar!=null)
                        espace.getChildren().add(telechar);
                }catch(Exception e){
                    Alertes.alerte("Erreur lors de l'ouverture", "Veuillez reessayer");
                }
            }
            else{
                if(ob.equals(envoi)){
                    try{
                        label.setText("Envoi");
                        espace.getChildren().clear();
                        changerCouleur(envoi,couleur2);
                        if(envoyer!=null)
                            espace.getChildren().add(envoyer);
                         
                    }catch(Exception e){
                        Alertes.alerte("Erreur lors de l'ouverture", "Veuillez reessayer");
                    }
                }
                else{
                    if(ob.equals(parametre)){
                        try{
                            label.setText("Parametres");
                            espace.getChildren().clear();
                            changerCouleur(parametre,couleur2);
                            if(param!=null)
                                espace.getChildren().add(param);
                        }catch(Exception e){
                             Alertes.alerte("Erreur lors de l'ouverture", "Veuillez reessayer");
                        }
                    }
                    else{
                        if(ob.equals(amis)){
                            try{
                                label.setText("Amis");
                                espace.getChildren().clear();
                                changerCouleur(amis,couleur2);
                                if(amis!=null)
                                    espace.getChildren().add(listeAmis);
                            }catch(Exception e){
                                System.out.println(e.getMessage());
                                 Alertes.alerte("Erreur lors de l'ouverture", "Veuillez reessayer");
                            }
                        }
                    }
                }
            }
        }
    }

    private AnchorPane chargerVue(String vue, FXMLLoader loader){
        AnchorPane np=new AnchorPane();
        try{
            loader.setLocation(getClass().getResource(vue));
            System.out.println(loader.getLocation().toString());
            if(loader.equals(loaderAmis))
                loaderAmis.setControllerFactory(c->{return new AmisController(parControl.getNomUtilisateur().getText());});
            if(loader.equals(loaderAccueil)){
                loaderAccueil.setControllerFactory(c->{return new ConnexionController(parControl,telControl.getListeTelechargement(),amisControl.getListeAmis(),amisControl.getConvers());});
            }
            else{
                if(loader.equals(loaderEnvoyer)){
                    loaderEnvoyer.setControllerFactory(c->{return new EnvoyerController(parControl);});
                }
                else{
                    if(loader.equals(loaderTelechar)){
                         loaderTelechar.setControllerFactory(c->{return new TelechargerController(parControl);});
                    }
                }
            }
            np=(AnchorPane)loader.load();
            return np;
        }
        catch(IOException e){
            e.printStackTrace();
            Alertes.alerte("Erreur lors du chargement", "Veuillez reessayer");
        }
        return null;
    }
    private void changerCouleur(AnchorPane np,String c){
        String style = "-fx-background-color: "+c+";";
        np.setStyle(style);
    }
    
    private void resetCouleur(){
              changerCouleur(connexion,couleur1);
              changerCouleur(amis,couleur1);
              changerCouleur(telechargement,couleur1);
              changerCouleur(envoi,couleur1);
              changerCouleur(parametre,couleur1);
    }
}