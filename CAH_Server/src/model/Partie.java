/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.Cartes;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author jcf_1
 */
public class Partie {
    private Joueur[] tabJoueur;
    private Stack stackNoire = new Stack();
    private Stack stackBlanche = new Stack();
    private Stack stackTourJoueur = new Stack();
    private ArrayList propositions = new ArrayList();
    private int nbCartesParJoueur = 10;

    public Partie() {
    }
   
    public void melangeCartes(){
        // initialisation des cartes à partir des fichiers .txt
        Cartes.getBlanchesFromTxt();
        Cartes.getNoiresFromTxt();
        // Mix up the white cards
        do{
            int randomCard = (int)(Math.random()* Cartes.listeBlanches.size());
            stackBlanche.add(Cartes.listeBlanches.get(randomCard));
            Cartes.listeBlanches.remove(randomCard);
        }while(!Cartes.listeBlanches.isEmpty());

        // Mix up the black cards
        do{
            int randomCard = (int)(Math.random()* Cartes.listeNoires.size());
            stackNoire.add(Cartes.listeNoires.get(randomCard));
            Cartes.listeNoires.remove(randomCard);
        }while(!Cartes.listeNoires.isEmpty());
    }
    
    public void distribuerCartes(){
        // distribue les cartes au début d'une partie
        // 10 cartes par joueur si le nombre de cartes disponibles
        // est supérieur ou égal au nombre de joueurs *10
        if (tabJoueur.length*10>Cartes.listeBlanches.size()){
            nbCartesParJoueur = (int)Math.floor(Cartes.listeBlanches.size()/tabJoueur.length);
        }
        for(int i=0; i<tabJoueur.length;i++){
            for(int j=0; j<nbCartesParJoueur;j++){
                tabJoueur[i].ajouterBlanche((Blanche)stackBlanche.pop());
            }
        }
    }
    public void nextNoire(){
        stackNoire.pop();
    }
    public Noire getCurrentNoire(){
        return (Noire)stackNoire.peek();
    }
    
    public void pigerCartes(){
        // repige le nombre de cartes nécessaires pour tous les joueurs
        for (int i=0;i<tabJoueur.length;i++){
            if (tabJoueur[i].getProvenance()!=getCurrentJoueur().getProvenance()){
                do {
                    tabJoueur[i].ajouterBlanche((Blanche)stackBlanche.pop());
                } while(tabJoueur[i].getNombreBlanches()<nbCartesParJoueur);
            }
        }
    }
    public void ordreInitiate(){
        List shadowListJoueur = new ArrayList();
        for (int i=0;i<tabJoueur.length;i++){
            shadowListJoueur.add(tabJoueur[i].getProvenance());
        }
        do{
            int randomJoueur = (int)(Math.random()* shadowListJoueur.size());
            stackTourJoueur.add(shadowListJoueur.get(randomJoueur));
            shadowListJoueur.remove(randomJoueur);
        }while(!shadowListJoueur.isEmpty());
    }
    
    public int getNbrPropositions(){
        return propositions.size();
    }
    public void flushPropositions(){
        propositions.clear();
    }
    public Boolean ajouterProposition(Proposition pr){
        return propositions.add(pr);
    }
    public Proposition getProposition(int i){
        return (Proposition)propositions.get(i);
    }
    public void nextJoueur(){
        stackTourJoueur.push(stackTourJoueur.pop());
    }
    public Joueur getCurrentJoueur(){
        /*for (int i=0;i<tabJoueur.length;i++){
            if (((Joueur)stackTourJoueur.peek()).getProvenance()==tabJoueur[i].getProvenance()) return tabJoueur[i];
        }*/
        return (Joueur)stackTourJoueur.peek();
    }
    
}
