/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cah_server;

import java.net.*;
import java.io.*;

/**
 *
 * @author d.bourcet
 */
public class CAH_Server {

    private ServerSocket serveurSock;
    private int port;
    private final int maxConnexions = 100;
    private Socket[] connexions = new Socket[maxConnexions];
    private int nbConnexions = -1;
    private PrintWriter[] os = new PrintWriter[maxConnexions];
    private BufferedInputStream[] is = new BufferedInputStream[maxConnexions];
    private VerifierServeur vt;
    
    public CAH_Server(int p) {
        port = p;
    }

    public CAH_Server() {
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

            nbConnexions++;
            
            //Message à l'usager
            System.out.println("Connexion " + num
                    + " sur le port #" + sk.getPort());
            
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
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
                    texte = (new String(buf)).trim();

                    //Déterminer la provenance (voir la méthode envoyer() du client):
                    provenance = Integer.parseInt(texte.substring(0, texte.indexOf("|")));

                    for (int z = 0; z <= nbConnexions; z++) {
                        //Ne pas renvoyer le message à l'expéditeur
                        if (z != provenance) {
                            this.envoyer(texte.substring(texte.indexOf("|") + 1), z);
                        }
                    }

                    //Vérifier si le client a demandé une déconnexion :
                    String msg = texte.substring(texte.indexOf(">>") + 2);
                    if (msg.equals("STOP")) {
                        try {
                            is[provenance].close();
                            os[provenance].close();
                            connexions[provenance].close();
                            is[provenance] = null;
                            os[provenance] = null;
                            connexions[provenance] = null;
                            System.out.println("TODO : Deconnecter " + texte);
                        } catch (Exception x) {
                            x.printStackTrace();
                        }
                    }

                    //Effacer le buffer
                    buf = null;

                }
            }
        } catch (IOException e) {
        }
    }
    public static void main(String[] args) {
        CAH_Server serveur = new CAH_Server();
        serveur.connecter();
    }
}

class VerifierServeur extends Thread {
    CAH_Server ref;
    public VerifierServeur(CAH_Server cs) {
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
    CAH_Server ref;
    public VerifierConnexion(CAH_Server cs) {
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