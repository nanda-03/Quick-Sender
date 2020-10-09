
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class Serveur extends Utilisateur{
    private Socket socket=null;
    private ServerSocket serversocket;
    private ScheduledService<Void> acceptService;
    private ScheduledService<Void> disconnectService;
    private ListView<HBox> listeAmis;
    Accept_Connexion acc;
    Deconnecter disc;
    
    //CONSTRUCTOR
    public Serveur(String s,ListView<HBox> listeAmis) throws IOException {
        super(s);
        this.listeAmis=listeAmis;
        serversocket=new ServerSocket(PORT);
        System.out.println("Serveur allumé"); 
    }
    public void acceptDisconnectServices(){
          acceptService = new ScheduledService<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {                    
                    @Override
                    protected Void call() throws Exception {
                        if(!serversocket.isClosed()){
                            acc= new Accept_Connexion(serversocket,nom,listeAmis);     
                            acc.lancer();
                        }
                        return null;
                    }
                };
            }
        };
        acceptService.setPeriod(Duration.millis(20));
        acceptService.start();
        disconnectService = new ScheduledService<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {                    
                    @Override
                    protected Void call() throws Exception {
                        disc=new Deconnecter(serversocket,listeAmis);
                        disc.lancer();
                        return null;
                    }
                };
            }
        };
        disconnectService.setPeriod(Duration.millis(20));
        disconnectService.start();
    }
    public void fermer() throws IOException{
        acceptService.cancel();
        disconnectService.cancel();
        Connexion.addr.clear();
        disc.vider();
        acc.close();
    }   
    public ServerSocket getServersocket() {
        return serversocket;
    }

    public void setServersocket(ServerSocket serversocket) {
        this.serversocket = serversocket;
    }

}
class Prendre_clients implements Runnable {
    private Socket socket ;
    private String nom;
    private PrintWriter out;
    private BufferedReader in;
    public Prendre_clients (Socket s,String nom ) {
        socket = s ;
        this.nom=nom;
    }
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

        @Override
        public void run ( ) {
            try {
                
                out=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
                in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
               // Thread recevoirMessage=new Thread(new ReceptionTexte(socket,in));
            //    recevoirMessage.start();
            } catch ( IOException e ) { e.printStackTrace(); }
        }
}