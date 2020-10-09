
package modele;

import controllers.AmisController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import principal.Alertes;


public class Deconnecter {
    ServerSocket serversocket=null;
    Socket socket=null;
    private ListView<HBox> listeAmis= new ListView<HBox>();
    public Deconnecter(ServerSocket s,ListView<HBox> listeAmis) {
        serversocket=s;
        this.listeAmis=listeAmis;
    }

    public void lancer() {
        for(int i=0; i<Connexion.addr.size();i++){
            if(!Connexion.addr.get(i).getSocket().isConnected()){
                String nom;
                try {
                    String s=Connexion.addr.get(i).getNom()+"    "+Connexion.addr.get(i).getSocket().getInetAddress().getHostName();
                    Alertes.notification("Information", Connexion.addr.get(i).getNom()+" s'est déconnecté");
                    retirer(Connexion.addr.get(i).getNom(),Connexion.addr.get(i).getSocket().getInetAddress().getHostAddress());
                    Connexion.addr.remove(Connexion.addr.get(i));
                    Connexion.addr.get(i).getSocket().close();
                    System.out.println(s+" est déconnecté");   
                } catch (IOException ex) {
                    Logger.getLogger(Deconnecter.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
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
    public void vider(){
        Connexion.addr.clear();
        listeAmis.getItems().clear();
    }
}
