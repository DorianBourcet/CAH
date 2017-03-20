/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Blanche;
//============================== Peut-etre pas necessaire
import model.Joueur;
//==============================
import model.Partie;
import model.Proposition;

/**
 *
 * @author d.bourcet
 */
public class MainServer {
    private ServerSocket serveurSock;
    private int port;
    //private final int maxConnexions = 100;
    //private Socket[] connexions = new Socket[maxConnexions];
    private ArrayList connexions = new ArrayList();
    private int nbConnexions = -1;
    private HashMap listeAlias = new HashMap();
    private ArrayList joueurStart = new ArrayList();
    private int nbrJoueurStart = 0;
    
    //private PrintWriter[] os = new PrintWriter[maxConnexions];
    private ArrayList os = new ArrayList();
    //private BufferedInputStream[] is = new BufferedInputStream[maxConnexions];
    private ArrayList is = new ArrayList();
    private VerifierServeur vt;
    
    private boolean partieCommencer = false;
    private Partie partie;
    
    public MainServer(int p) {
        port = p;
    }

    public MainServer() {
        this(5555);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int p) {
        port = p;
    }
    
    private void envoyer(String msg, int i) {
        if (os.get(i) == null) {
            return;
        }
        ((PrintWriter)os.get(i)).print(msg);
        ((PrintWriter)os.get(i)).flush();
    }
    
    private void connecter() {
        try {
            //Création du socket
            serveurSock = new ServerSocket(port);

            //Démarrer l'inspecteur
            VerifierConnexion vc = new VerifierConnexion(this);
            vc.start();

            //Message à l'usager
            System.out.println("Serveur " + serveurSock
                    + " a l\'ecoute sur le port #" + serveurSock.getLocalPort());

        } catch (IOException e) {
            System.out.println("\nServeur déjà actif sur ce port...");
        }
    }
    
    public void attente() {
        try {
            int num = nbConnexions + 1;

            //Attente d'une connection :
            Socket sk = serveurSock.accept();

            //Mémorisation de la connection
            connexions.add(num,sk);

            //Initialisation des entrées/sorties :
            os.add(num,new PrintWriter(((Socket)connexions.get(num)).getOutputStream()));
            is.add(num,new BufferedInputStream(((Socket)connexions.get(num)).getInputStream()));

            //Première connection?
            if (num == 0) {
                //Oui, démarrer le thread inspecteur de texte :
                vt = new VerifierServeur(this);
                vt.start();
            }

            //Envoyer le numéro de la connection au client
            this.envoyer(String.valueOf(num), num);
            //this.envoyer("Bienvenue sur le serveur",num);

            nbConnexions++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int z = 0; z <= nbConnexions; z++) {
                this.envoyer((nbConnexions+1)+" joueurs connectés (dont vous)", z);
                }
            
            //Message à l'usager
            System.out.println("Connexion " + num
                    + " sur le port #" + sk.getPort());
            
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    /*public boolean verifyListStartJoueur(int provenance){
        if (nbrJoueurStart==0) return false;
        for (int l = 0; l <= nbrJoueurStart; l++) {
            if (joueurStart[l] == provenance) return true;
        }
        return false;
    }*/
    public void lire() {
        try {
            //buffer de lecture
            byte buf[] = new byte[500];
            String texte;
            int provenance; //provenance du texte

            //Lire toutes les connexions
            for (int i = 0; i <= nbConnexions; i++) {
                //La connection est-elle active?
                if (is.get(i) != null && ((BufferedInputStream)is.get(i)).available() > 0) {
                    //Oui, lire le socket
                    ((BufferedInputStream)is.get(i)).read(buf);
                    texte = new String(buf);
                    System.out.println("Recu : "+texte);
                    //Déterminer la provenance (voir la méthode envoyer() du client):
                    provenance = Integer.parseInt(texte.substring(0, texte.indexOf("|")));
                    String alias = texte.substring(texte.indexOf("|")+1,texte.indexOf(">>"));
                    listeAlias.put(provenance,alias);
                    System.out.println(listeAlias.get(provenance));
                    String commande;
                    if (texte.indexOf(" ")!=-1) {
                        commande = texte.substring(texte.indexOf(">>")+2,texte.indexOf(" "));
                    } else {
                        commande = " ";
                    }
                    System.out.println("commande "+commande);
                    String msg = texte.substring(texte.indexOf(" ")+1);
                    
                    switch(commande){
                        case "PROPOSITION":
                            if (partieCommencer == true){
                                Proposition propo = new Proposition(provenance);

                                if (partie.getCurrentNoire().getPiger() >= 2){
                                    String propositions[];
                                    propositions = msg.split(" ");
                                    for(int y = 0; y <propositions.length;y++){
                                        propo.ajouterBlanche(partie.getJoueur(provenance).getBlanches().get(propositions[y]));
                                        partie.getJoueur(provenance).deleteBlanche(propositions[y]);
                                    }
                                }else{
                                    String proposition = msg;
                                    propo.ajouterBlanche(partie.getJoueur(provenance).getBlanches().get(proposition));
                                    partie.getJoueur(provenance).deleteBlanche(proposition);
                                }
                                partie.ajouterProposition(propo);
                                if(partie.getNbrPropositions()== joueurStart.size()-1){
                                    this.broadcast("Voici les propositions:\n","Veuillez choisir la meilleure proposition\nÀ l'aide de la commande VOTE <votre_choix>", partie.getCurrentJoueur().getProvenance() );
                                    for (int y = 0; y <partie.getNbrPropositions();y++){
                                        this.broadcast((y+1)+": "+partie.getProposition(y).toString()+"\n\n");
                                    }
                                }
                            }
                            break;
                        case "VOTE":
                            if(partieCommencer == true){
                                if(partie.getCurrentJoueur().getProvenance() == provenance && partie.getNbrPropositions()== joueurStart.size()-1){
                                    partie.getJoueur(partie.getProposition((Integer.parseInt(msg))-1).getIdJoueur()).incrementerScore();
                                    this.broadcast("La proposition "+msg+" a été choisie, "+getJoueur(partie.getProposition(Integer.parseInt(msg)).getIdJoueur()).getAlias()+" gagne un point!");
                                    partie.pigerCartes();
                                    partie.nextJoueur();
                                    partie.nextNoire();
                                    partie.flushPropositions();
                                    Joueur currentJoueur = partie.getCurrentJoueur();
                                    this.broadcast("C'est vôtre tour pour piger une carte noir!", currentJoueur.getAlias()+" est le prochain joueur à piger une carte noir!", currentJoueur.getProvenance());
                                    this.broadcast(partie.getCurrentNoire().getTexte());
                                    this.broadcast("Vous devez selectionner "+partie.getCurrentNoire().getPiger()+" carte(s)","Attente de proposition...",currentJoueur.getProvenance());
                                }
                            }
                            break;
                        case "CHAT":
                            for (int z = 0; z <= nbConnexions; z++) {
                                if (z != provenance) {
                                    this.envoyer(alias+">>"+msg, z);
                                }
                            }
                            break;
                        case "START":
                            if (nbConnexions+1 >= 3) {
                                if (joueurStart.indexOf(provenance)==-1){
                                    joueurStart.add(provenance);
                                    nbrJoueurStart++;
                                    System.out.println("length connexions : "+connexions.size());
                                    System.out.println("joueurStart.size() "+joueurStart.size());
                                    if (connexions.size() == joueurStart.size()){
                                        this.envoyer("Il ne manquait plus que vous ! Partie démarrée !",provenance);
                                    } else {
                                        this.envoyer("Demande de confirmation des autres joueurs...", provenance);
                                    }
                                    /*for (int k = 0; k <= nbrJoueurStart; k++) {
                                        if (joueurStart[k] == 0) joueurStart[k] = provenance;
                                    }*/
                                }
                                for (int z = 0; z <= nbConnexions; z++) {
                                    if (z != provenance) {
                                        if (connexions.size() == joueurStart.size()){
                                            this.envoyer("Tout le monde est là :) Partie démarrée", z);
                                        } else {
                                            if (joueurStart.indexOf(z)!=-1){
                                            this.envoyer("Un nouveau joueur vient d'accepter",z);
                                        } else {
                                            this.envoyer((nbrJoueurStart)+" joueurs veulent commencer la partie."
                                                +"\n"+ " Veuillez inscrire START.", z);
                                        }
                                        }
                                    }
                                }
                            }else this.envoyer("Pas assez de joueurs en ligne.", provenance);
                            
                            break;
                        case "STOP":
                            try {
                                /*for (int z = 0; z <= nbrJoueurStart; z++) {
                                    if (joueurStart[nbrJoueurStart] == provenance) joueurStart[nbrJoueurStart] = 0;
                                    nbrJoueurStart--;
                                }*/
                                ((BufferedInputStream)is.get(provenance)).close();
                                ((PrintWriter)os.get(provenance)).close();
                                ((Socket)connexions.get(provenance)).close();
                                is.set(provenance,null);
                                os.set(provenance,null);
                                connexions.set(provenance,null);
                                nbConnexions--;
                                
                                for (int z = 0; z <= nbConnexions; z++) {
                                    this.envoyer((nbConnexions+1)+" joueurs connectés (dont vous)", z);
                                }
                                System.out.println("TODO : Deconnecter " + texte);
                            } catch (Exception x) {
                                x.printStackTrace();
                            }
                            break;
                        case "HELP":
                            String help = "=== HELP ===\n"
                                    + "Available commands\n"
                                    + " - START to start a game\n"
                                    + " - CHAT and your message to chat with connected players\n"
                                    + " - STOP to close your connection to the game\n";
                            this.envoyer(help, provenance);
                            break;
                        default:
                    }
                    //Initialisation d'une partie
                    if (connexions.size() == joueurStart.size() && partieCommencer == false){
                        initiatePartie();
                    }
                    //Effacer le buffer
                    buf = null;
                }
            }
        } catch (IOException e) {
        }
    }
    public static void main(String[] args) {
        MainServer serveur = new MainServer();
        serveur.connecter();
    }

    private void initiatePartie() {
        partieCommencer = true;
        partie = new Partie(joueurStart.size());
        //TODO Méthode pour inséré les joueurs de la room dans la partie
        //idée 1: Une boucle for qui met les joueurs un par un avec une methode setJoueur(Joueur player)
        //idée 2: une méthode qui prend un tableau de joueur et qui set les joueurs de la room à la partie avec un méthode setJoueurs(Joueur[] ou arraylist)
        partie.melangeCartes();
        partie.ordreInitiate();
        partie.distribuerCartes();
        //voir getCurrentJoueur pour le retour
        Joueur currentJoueur = partie.getCurrentJoueur();
        this.broadcast("Vous êtes le premier joueur à piger une carte noir!", currentJoueur.getAlias()+" est le premier joueur à piger une carte noir!", currentJoueur.getProvenance());
        this.broadcast(partie.getCurrentNoire().getTexte());
        this.broadcast("Vous devez selectionner "+partie.getCurrentNoire().getPiger()+" carte(s)");
    }
    private void broadcast(String message){
        for (int z = 0; z <= joueurStart.size(); z++) {
            this.envoyer(message, z);
        }
    }
    private void broadcast(String messageAll, String messageProvenance, int provenance){
        for (int z = 0; z <= joueurStart.size(); z++) {
            if(provenance== z) this.envoyer(messageProvenance, z);
            else this.envoyer(messageAll, z);
        }
    }
}

class VerifierServeur extends Thread {
    MainServer ref;
    public VerifierServeur(MainServer cs) {
        ref = cs;
    }
    public void run() {
        while (true) {
            ref.lire();
            try {
                //Laisser une chance d'exécution aux autres threads
                Thread.sleep(10);
            } catch (Exception x) {
            }
        }
    }
}

class VerifierConnexion extends Thread {
    MainServer ref;
    public VerifierConnexion(MainServer cs) {
        ref = cs;
    }
    public void run() {
        while (true) {
            ref.attente();
            try {
                //Laisser une chance d'exécution aux autres threads
                Thread.sleep(10);
            } catch (Exception x) {
            }
        }
    }
}