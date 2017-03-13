/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;



/**
 *
 * @author soixa
 */
public class Joueur {
    
    private String alias;
    private int provenance;
    private List<Blanche> blanches;

    public Joueur(String alias, int provenance, List<Blanche> blanches) {
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

    public List<Blanche> getBlanches() {
        return blanches;
    }

    public void setBlanches(List<Blanche> blanches) {
        this.blanches = blanches;
    }
    
}
