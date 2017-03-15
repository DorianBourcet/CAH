
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Blanche;
import model.Noire;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author soixa
 */

// Permet d'obtenir les cartes depuis les fichiers txt du dossier resources.
public class Cartes {
    public static ArrayList listeBlanches = new ArrayList();
    public static ArrayList listeNoires = new ArrayList();
    
    public static ArrayList getBlanchesFromTxt(){
        String line;
        try {
            FileInputStream fis = new FileInputStream(new File("./resources/blanches.txt"));
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine())!= null) {
                Blanche blanche = new Blanche(line);
                System.out.println(line);
                listeBlanches.add(blanche);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cartes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listeBlanches;
    }
    
    public static ArrayList getNoiresFromTxt(){
        String line;
        try {
            FileInputStream fis = new FileInputStream(new File("./resources/noires.txt"));
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine())!= null) {
                String[] parts = line.split(";");
                Noire noire = new Noire(parts[0], Integer.parseInt(parts[1]));
                System.out.println(line);
                listeNoires.add(noire);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cartes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listeNoires;
    }
}
