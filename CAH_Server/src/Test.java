
import java.util.ArrayList;
import model.Joueur;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author soixa
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //ArrayList test = Cartes.getNoiresFromTxt();
        //System.out.println(Cartes.listeNoires.get(5).toString());
        Joueur j1 = new Joueur();
        System.out.println(j1.incrementerScore());
        System.out.println(j1.toString());
    }
    
}
