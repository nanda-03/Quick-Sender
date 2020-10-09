/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ESDRAS
 */
public class TestClient {
    public static void main(String[] args){
        StringProperty chem= new SimpleStringProperty("D:");
        try {
            Client client=new Client("Esdras",InetAddress.getLocalHost().getHostAddress(),chem);
        } catch (IOException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
