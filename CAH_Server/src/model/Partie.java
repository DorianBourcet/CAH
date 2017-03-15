/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

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

    public Partie() {
    }
   
    public void melangeCartes(){
        //retreave white card from file  TODO
        List listBlanche = new ArrayList();
        do{
            int randomCard = (int)(Math.random()* listBlanche.size());
            stackBlanche.add(listBlanche.get(randomCard));
            listBlanche.remove(randomCard);
        }while(listBlanche.isEmpty());
        
        //retreave black card from file   TODO
        List listNoire = new ArrayList();
        do{
            int randomCard = (int)(Math.random()* listNoire.size());
            stackNoire.add(listNoire.get(randomCard));
            listNoire.remove(randomCard);
        }while(listNoire.isEmpty());
    }
    
    public void distribuerCartes(){
        for(int i =0; i<tabJoueur.lenght();i++){
            for(int j =0; j<10;j++)
                {
                    tabJoueur[i].blanches.push(stackBlanche.pop());
                }
        }
    }
    public void pigeCarte(int piger){
        for (int i =0; i<tabJoueur.lenght();i++){
            if (tabJoueur[i].blanches.size()!= 10) 
                for (int j =0; j<piger;j++)tabJoueur[i].blanches.push(stackBlanche.pop());
        }
    }
    public void ordreInitiate(){
        List shadowListJoueur = new ArrayList();
        for (int i=0;i<tabJoueur.lenght();i++){
            shadowListJoueur.add(tabJoueur[i].provenance);
        }
        do{
            int randomJoueur = (int)(Math.random()* shadowListJoueur.size());
            stackTourJoueur.add(shadowListJoueur.get(randomJoueur));
            shadowListJoueur.remove(randomJoueur);
        }while(shadowListJoueur.isEmpty());
    }
    public void nextJoueur(){
        stackTourJoueur.push(stackTourJoueur.pop());
    }
    public Joueur getCurrentJoueur(){
        for (int i=0;i<tabJoueur.lenght();i++){
            if (stackTourJoueur.peek()==tabJoueur[i].provenance) return tabJoueur[i];
        }
        return null;
    }
    
}
