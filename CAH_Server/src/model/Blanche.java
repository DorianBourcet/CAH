/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author soixa
 */
public class Blanche {
    private int idJoueur;
    private String texte;

    public Blanche(int idJoueur, String texte) {
        this.idJoueur = idJoueur;
        this.texte = texte;
    }
    public Blanche() {
        this(-1,"");
    }

    public int getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }
    
}
