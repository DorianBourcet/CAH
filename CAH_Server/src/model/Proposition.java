/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author soixa
 */
public class Proposition {
    
    private int idJoueur;
    private ArrayList blanches = new ArrayList();

    public Proposition(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public int getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public ArrayList getBlanches() {
        return blanches;
    }

    public void setBlanches(ArrayList blanches) {
        this.blanches = blanches;
    }
    
    public Boolean ajouterBlanche(Blanche bl){
        return this.blanches.add(bl);
    }
    
    public String toString(){
        Iterator itr = this.blanches.iterator();
        String str = "";
        while(itr.hasNext()){
            str+=((Blanche)itr.next()).getTexte()+"\n";
        }
        return str;
    }
    
}
