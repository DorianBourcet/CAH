/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author soixa
 */
public class Joueur implements Serializable {
    
    private String alias;
    private int provenance;
    private int score = 0;
    private ArrayList<Blanche> blanches;

    public Joueur(String alias, int provenance, int score, ArrayList<Blanche> blanches) {
        this.alias = alias;
        this.provenance = provenance;
        this.blanches = blanches;
        this.score = score;
    }
    
    public Joueur(String alias, int provenance, ArrayList<Blanche> blanches) {
        this.alias = alias;
        this.provenance = provenance;
        this.blanches = blanches;
    }

    public Joueur(String alias, int provenance) {
        this.alias = alias;
        this.provenance = provenance;
    }

    public Joueur() {
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getProvenance() {
        return provenance;
    }

    public void setProvenance(int provenance) {
        this.provenance = provenance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Blanche> getBlanches() {
        return blanches;
    }

    public void setBlanches(ArrayList<Blanche> blanches) {
        this.blanches = blanches;
    }
    
    public Boolean addBlanche(Blanche bl){
        return this.blanches.add(bl);
    }
    
    public Boolean deleteBlanche(Blanche bl){
        return this.blanches.remove(bl);
    }
    
    public int getNombreBlanches(){
        return this.blanches.size();
    }
    
    public int incrementerScore(){
        return ++score;
    }
    
    public String toString(){
        return ""+this.score;
    }
}
