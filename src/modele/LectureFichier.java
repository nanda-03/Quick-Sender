/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import principal.Alertes;

/**
 *
 * @author ESDRAS
 */
public class LectureFichier {
    public static BufferedReader fis= null;
    public static String repertoire="./src/profil.txt";
    public static String[] retournerValeur(){
        try{
            fis = new BufferedReader(new BufferedReader(new FileReader(repertoire)));
            String ligne;
            String[] tab={ new String(), new String()};
            int i=0;
            do{
                ligne=fis.readLine();
                if(ligne!=null && i<2)
                    tab[i]=ligne;
                i++;    
            }while(ligne!=null);
            fis.close();
            return tab;
        }
        catch(Exception e){
            Alertes.alerte("Impossible de lire", "Nous n'avons pas pu récupéré toutes vos données");
        }
        return null;
    }
 /*   public static int lire(Text reperDownload, TextField nomUser){
        int j=0;
        try{
            fis = new BufferedReader(new BufferedReader(new FileReader(repertoire)));
            String ligne;
            String[] tab={ new String(), new String()};
            int i=0;
            do{
                ligne=fis.readLine();
                if(ligne!=null && i<2)
                    tab[i]=ligne;
                i++;    
            }while(ligne!=null);
            if(!tab[0].equals(""))
                nomUser.setText(tab[0]);
            else
                nomUser.setText("Inconnu");
            if(!tab[1].equals(""))
                reperDownload.setText(tab[1]);
            else
                reperDownload.setText("C:");
            fis.close();
        }
        catch(Exception e){
            j=-1;
            nomUser.setText("Inconnu");
            reperDownload.setText("C:");
        }
        return j;
    }*/
    public static int ecrire(String nomU, String reper){
        List<String> lignes= Arrays.asList(nomU,reper); 
        Path fichier = Paths.get(repertoire);
        int i=0;
        try {
            if(!(nomU.equals("") || reper.equals("")))
                Files.write(fichier,lignes,Charset.forName("UTF-8"));
            else{
                i=-1;
            }
        } catch (Exception ex) {
            i=-1;
        }
        return i;
    }
}
