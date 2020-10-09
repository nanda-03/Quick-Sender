/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.awt.Color;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author ESDRAS
 */
public class Progression {

    public static void genererProgression(String objet, String nomPers, String nomFichier, DoubleProperty progress){
        Group root = new Group();
        Stage stage = new Stage(); 
        stage.initModality(Modality.NONE); 
        stage.setOpacity(1); 
        stage.setTitle(objet); 
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(12));
        root.getChildren().add(mainPane);
        Label objetLabel = new Label(objet);
        Label nomPersLabel = new Label(nomFichier);
        nomPersLabel.setMaxWidth(250);
        nomPersLabel.setWrapText(true);
        HBox hb=new HBox();
        hb.setSpacing(10);
        hb.getChildren().add(objetLabel);
        hb.getChildren().add(nomPersLabel);
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(progress);
        /*progress.addListener((ob,oldVal,newVal)->{
            if(newVal!=null && newVal.doubleValue()==1.0){
                stage.close();
            }
        });*/
        VBox hb2=new VBox();
        hb2.setAlignment(Pos.BOTTOM_LEFT);
        String s;
        if(objet.equals("Réception du fichier"))
            s="De ";
        else
            s="A ";
        hb2.getChildren().add(new Label(s+nomPers));
        hb2.setSpacing(5);
        hb2.getChildren().add(progressBar);
        progressBar.setPrefWidth(350);
        progressBar.setStyle("-fx-accent: green");
        mainPane.setTop(hb);
        mainPane.setBottom(hb2);
        Scene scene = new Scene(root, 400, 120);
        stage.setScene(scene); 
        stage.showAndWait(); 
    }
}
