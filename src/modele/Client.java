package modele;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;


public class Client extends Utilisateur{
        
    private Socket socketFile;
    private String adresse;
    private Thread t;
    private ReceptionFichier recepFile;
    private StringProperty cheminReper=new SimpleStringProperty();

    public Client(String nom,String adresse,StringProperty cheminReper, ListView<HBox> listeTelechargement, VueConversation convers) throws IOException {
        super(nom);
        this.adresse=adresse;
        this.cheminReper=cheminReper;
        socketFile=seConnecterFile();
        if(socketFile!=null){
            PrintWriter out=new PrintWriter(socketFile.getOutputStream());
            out.println(nom);
            out.flush();
            recepFile= new ReceptionFichier(socketFile, cheminReper.getValue(), listeTelechargement,convers);
        }else{
            recepFile=null;
        }

       
    }
    //partie qu'on peut supprimer
    public Client(String nom,String adresse,StringProperty cheminReper) throws IOException {
        super(nom);
        this.adresse=adresse;
        System.out.println("Client connecté");
        this.cheminReper=cheminReper;
        socketFile=seConnecterFile();
        recepFile= new ReceptionFichier(socketFile, cheminReper.getValue());
        recepFile.lancer2();
        /*final ScheduledService<Void> recepService = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        recepFile.lancer2();
                        return null;
                    }
                };
            }
            };
            recepService.setPeriod(Duration.seconds(1));
            recepService.start();*/
    }
    //fin de la partie
    public void lancer(){
        if(recepFile!=null){
        t=new Thread(recepFile);
        t.start();}
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public ReceptionFichier getRecepFile() {
        return recepFile;
    }

    public void setRecepFile(ReceptionFichier recepFile) {
        this.recepFile = recepFile;
    }

    public StringProperty getCheminReper() {
        return cheminReper;
    }

    public void setCheminReper(StringProperty cheminReper) {
        this.cheminReper = cheminReper;
    }

    public Socket getSocketFile() {
        return socketFile;
    }

    public void setSocketFile(Socket socketFile) {
        this.socketFile = socketFile;
    }

    public Socket seConnecterFile() {

        Socket socketFile;
        try {
            socketFile = new Socket(adresse, PORT);
            return socketFile;
        } catch (IOException ex) {
        }
 
        return null;
    }
    //DEFINIR LES METHODES SECONNECTE-RFILE


}
