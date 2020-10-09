/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import principal.Alertes;


public class Accept_Connexion {

    private ServerSocket serverS=null;
    private Socket socket=null;
    private String nom;
    private BufferedReader in;
    private ListView<HBox> listeAmis= new ListView<HBox>();
    private GestionClient gestClient;
  //  public Thread t1;
    //CONSTRUCTOR 
    public Accept_Connexion(ServerSocket serv,String nom,ListView<HBox> listeAmis){
        serverS=serv;
        this.nom=nom;
        this.listeAmis=listeAmis;
    }
    private boolean estAbsentListeSocket(Socket socket) {
        for(Profil soc: Connexion.addr){
            if(soc.getSocket().getInetAddress().getHostAddress().equals(socket.getInetAddress().getHostAddress())){
                return false;
            }
        }
        return true;
    }
    public void lancer() {
        try {
            
            socket = serverS.accept();
            if(estAbsentListeSocket(socket)){
                in=new BufferedReader(new InputStreamReader (socket.getInputStream()));
                String s=in.readLine();
                Profil p=new Profil(socket,s);
                Connexion.addr.add(p);
               // AmisController.ajouter(s, socket.getInetAddress().getHostAddress());
               Platform.runLater(()->{
                    HBox hbox=new HBox();
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setSpacing(8);
                    hbox.setPadding(new Insets(4));
                    VBox vbox=new VBox();
                    vbox.setAlignment(Pos.TOP_LEFT);
                    vbox.setSpacing(5);
                    hbox.getChildren().add(new ImageView("/image/User.png"));
                    Text nomT=new Text(s);
                    vbox.getChildren().add(nomT);
                    vbox.getChildren().add(new Text(socket.getInetAddress().getHostAddress()));
                    hbox.getChildren().add(vbox);
                    listeAmis.getItems().add(hbox);
                    Alertes.notification("Information", s+" s'est connecté");
               });
            }
            else{
                ReceptionFichier cl=GestionClient.estEgal(socket.getInetAddress().getHostAddress());
                if(cl!=null){
                    ReceptionFichier recep=new ReceptionFichier(socket,cl);
                    recep.recevoirFichier();
                }
            }
            System.out.println("Ip connectés");
            for(int i=0;i<Connexion.addr.size();i++){
                System.out.println(Connexion.addr.get(i).getSocket().getInetAddress().getHostAddress()+"    ");
            }
          //  t1 = new Thread ( new Prendre_clients(socket,nom)) ;
           // t1.start() ;
        } catch (IOException ex) {
            System.out.println("Heuuuuuuuuuuuu");
            /*Logger.getLogger(Accept_Connexion.class.getName()).log(Level.SEVERE, null, ex);*/
        }
    }
    public void close(){
        try {
            serverS.close();
        } catch (IOException ex) {
            Logger.getLogger(Accept_Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ServerSocket getServerS() {
        return serverS;
    }
    public void setServerS(ServerSocket serverS) {
        this.serverS = serverS;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    
}
