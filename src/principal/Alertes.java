package principal;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ESDRAS
 */
public class Alertes {
    public static void alerte(String entete,String mess){
        Alert alerte=new Alert(Alert.AlertType.WARNING);
        alerte.setTitle(entete);
        alerte.setHeaderText(null);
        alerte.setContentText(mess);
        alerte.showAndWait();
    }
    public static void notification(String titre, String texte){
        Notifications notification =Notifications.create().title(titre).text(texte).position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(7));
        notification.showInformation();
    }
}
