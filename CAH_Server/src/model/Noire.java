/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author soixa
 */
public class Noire implements Serializable {
    private String texte;
    private int piger;

    public Noire(String texte, int piger) {
        this.texte = texte;
        this.piger = piger;
    }
    
    public Noire() {
        this("",-1);
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public int getPiger() {
        return piger;
    }

    public void setPiger(int piger) {
        this.piger = piger;
    }
    
    public String toString(){
        return "TEXTE : "+this.texte+"\nPIGER : "+this.piger;
    }
}
