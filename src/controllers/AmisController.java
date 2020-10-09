/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import modele.Connexion;
import modele.EmissionFichier;
import modele.Profil;
import modele.Serveur;
import modele.VueConversation;
import modele.VueConversation;
import principal.Alertes;

/**
 * FXML Controller class
 *
 * @author ESDRAS
 */
public class AmisController implements Initializable {
    private String nomExpe;
    @FXML
    private ListView<HBox> listeAmis=new ListView<HBox>();
    private static String image ="/image/User.png";
    private VueConversation convers=new VueConversation("Message");
    @FXML
    private VBox root;
    @FXML
    private VBox roott;
    @FXML
    private AnchorPane conteneurChat;
    @FXML
    private TextField champMsg;
    private Serveur serveur;
    public AmisController(String n){
        nomExpe=n;
        listeAmis=new ListView<HBox>();
        convers=new VueConversation("Message");
    }

    public ListView<HBox> getListeAmis() {
        return listeAmis;
    }

    public VueConversation getConvers() {
        return convers;
    }

    public void setConvers(VueConversation convers) {
        this.convers = convers;
    }
    
    public void setListeAmis(ListView<HBox> listeAmis) {
        this.listeAmis = listeAmis;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        AmisController.image = image;
    }
    
    public void ajouter(String nom,String ip){
        System.out.println("amis 1");
        Platform.runLater(()->{
            HBox hbox=new HBox();
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setSpacing(8);
            hbox.setPadding(new Insets(4));
            VBox vbox=new VBox();
            vbox.setAlignment(Pos.TOP_LEFT);
            vbox.setSpacing(5);
            hbox.getChildren().add(new ImageView(image));
            Text nomT=new Text(nom);
            vbox.getChildren().add(nomT);
            vbox.getChildren().add(new Text(ip));
            hbox.getChildren().add(vbox);
            listeAmis.getItems().add(hbox);
            System.out.println("amis 2");
            System.out.println(listeAmis.getItems().isEmpty()?"la liste est vide":"la liste n'est pas vide");
        });
    }
    public void retirer(String nom,String ip){
        Platform.runLater(()->{
            for(HBox h: listeAmis.getItems()){
               if(((Text)(((VBox)(h.getChildren().get(1))).getChildren().get(0))).getText().equals(nom) && 
                       ((Text)(((VBox)(h.getChildren().get(1))).getChildren().get(1))).getText().equals(ip))
                   listeAmis.getItems().remove(h);
            }
        });        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        convers.prefHeightProperty().bind(conteneurChat.prefHeightProperty());
        convers.prefWidthProperty().bind(conteneurChat.prefWidthProperty());
        conteneurChat.getChildren().add(convers);
                //sendMessage("bonjour brisco");
        
    }    

    @FXML
    private void envoyerMsg(MouseEvent event) {
        if(champMsg.getText().length()!=0){
            String msg=new String(champMsg.getText());
            convers.sendMessage(msg);
            EmissionFichier emiFichier;
            for(Profil p: Connexion.addr){
                emiFichier=new EmissionFichier(p.getSocket(),nomExpe);
                try {
                    emiFichier.envoyerMessage(msg);
                } catch (IOException ex) {
                    Alertes.alerte("Impossible d'envoyer le message","Impossible d'envoyer le message à "+p.getNom());
                    Logger.getLogger(AmisController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            champMsg.setText("");
        }
    }
    
}
