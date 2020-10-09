/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author ESDRAS
 */
public class Transfer extends Application {
    public static Stage primaryStage;
    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage=stage;
        FXMLLoader loader=new FXMLLoader();  
        loader.setLocation(getClass().getResource("Principal.fxml"));
        Parent root = loader.load();
        stage.setTitle("QuickSend");
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
