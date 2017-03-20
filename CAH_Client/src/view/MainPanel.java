/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.Client;
import javax.swing.*;
import javax.swing.border.*; //pour les bordures

import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * @author A. Toudeft
 */
public class MainPanel extends JPanel implements Observer {

//  Modele :
    Client client;
//	Composants pour l'interface :
//	Panneaux:
    JPanel centerPanel;

//	Composants:	
    JTextField tSaisie;
    JTextArea taSalon;

    MainPanelActionListener mpal = new MainPanelActionListener();
    MainPanelKeyListener mpkl = new MainPanelKeyListener();

    /**
     *
     */
    public MainPanel() {
        super();
//		Panneaux et composants:	 	 
        centerPanel = new JPanel();
        Border bordure1 = BorderFactory.createLineBorder(Color.blue, 1);
        tSaisie = new JTextField(10);
        taSalon = new JTextArea(20, 10);

        tSaisie.addKeyListener(mpkl);

        taSalon.setBorder(bordure1);
        taSalon.setEditable(false);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(tSaisie, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(taSalon), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(centerPanel);

        setVisible(false);

    }

    public void update(Observable o, Object t) {
        if (o instanceof Client) {
            if (client == null) {
                client = (Client) o;
            }

            String s = ((Client) o).getEtat();

            if (t != null) {
                taSalon.append("\n" + t);
            } else if (s.equals("RECH_SERVEUR")) {
                setVisible(true);
                taSalon.append("\nRecherche d'un serveur...");
            } else if (s.equals("CONNECTION")) {
                taSalon.append("\nConnection �tablie");
                taSalon.append("\n------------------");
            } else if (s.equals("DECONNECTE")) {
                setVisible(false);
            }
        }
    }

    class MainPanelActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            Object source = evt.getSource();
        }
    }

    class MainPanelKeyListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String s = tSaisie.getText();
                if (!s.equals("")) {
                    client.envoyer(s);
                    taSalon.append("\n>" + s);
                    tSaisie.setText("");
                }
            }
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }
}

