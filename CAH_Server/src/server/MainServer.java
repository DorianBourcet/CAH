/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author d.bourcet
 */
public class MainServer {
    private ServerSocket serveurSock;
    private int port;
    private final int maxConnexions = 100;
    private Socket[] connexions = new Socket[maxConnexions];
    private int nbConnexions = -1;
    
    private ArrayList joueurStart = new ArrayList();
    private int nbrJoueurStart = 0;
    
    private PrintWriter[] os = new PrintWriter[maxConnexions];
    private BufferedInputStream[] is = new BufferedInputStream[maxConnexions];
    private VerifierServeur vt;
    
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
        if (os[i] == null) {
            return;
        }
        os[i].print(msg);
        os[i].flush();
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
            connexions[num] = sk;

            //Initialisation des entrées/sorties :
            os[num] = new PrintWriter(connexions[num].getOutputStream());
            is[num] = new BufferedInputStream(connexions[num].getInputStream());

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

            //Lire toutes les connections
            for (int i = 0; i <= nbConnexions; i++) {
                //La connection est-elle active?
                if (is[i] != null && is[i].available() > 0) {
                    //Oui, lire le socket
                    is[i].read(buf);
                    texte = new String(buf);
                    System.out.println("Recu : "+texte);
                    //Déterminer la provenance (voir la méthode envoyer() du client):
                    provenance = Integer.parseInt(texte.substring(0, texte.indexOf("|")));
                    String alias = texte.substring(texte.indexOf("|")+1,texte.indexOf(">>"));
                    String commande;
                    if (texte.indexOf(" ")!=-1) {
                        commande = texte.substring(texte.indexOf(">>")+2,texte.indexOf(" "));
                    } else {
                        commande = " ";
                    }
                    System.out.println("commande "+commande);
                    switch(commande){
                        case "CHAT":
                            String msg = texte.substring(texte.indexOf(" ")+1);
                            for (int z = 0; z <= nbConnexions; z++) {
                                if (z != provenance) {
                                    this.envoyer(alias+">>"+msg, z);
                                }
                            }
                            break;
                        case "START":
                            if (nbConnexions+1 >= 3) {
                                //System.out.println(""+joueurStart.length);
                                if (joueurStart.indexOf(provenance)==-1){
                                    joueurStart.add(provenance);
                                    nbrJoueurStart++;
                                    this.envoyer("Demande de confirmation des autres joueurs...", provenance);
                                    /*for (int k = 0; k <= nbrJoueurStart; k++) {
                                        if (joueurStart[k] == 0) joueurStart[k] = provenance;
                                    }*/
                                }
                                for (int z = 0; z <= nbConnexions; z++) {
                                    if (z != provenance) {
                                        if (joueurStart.indexOf(z)!=-1){
                                            this.envoyer("Un nouveau joueur vient d'accepter",z);
                                        } else {
                                            this.envoyer((nbrJoueurStart)+" joueurs veulent commencer la partie."
                                                +"\n"+ " Veuillez inscrire START.", z);
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
                                is[provenance].close();
                                os[provenance].close();
                                connexions[provenance].close();
                                is[provenance] = null;
                                os[provenance] = null;
                                connexions[provenance] = null;
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