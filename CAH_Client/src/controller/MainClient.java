/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Client;
import view.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * @author A. Toudeft
 */
public class MainClient extends JFrame {
//Le mod�le:

    Client cl;

//La vue:
    MainPanel mp;

//Gestionnaire des �v�nements de menu:
    ActionListener mml = new MainMenuListener();

//Menus:
    JMenuBar mBar = new JMenuBar();
    MenuDemarrer mDemarrer = new MenuDemarrer();
    MenuTaches mTaches = new MenuTaches();
    MenuAide mAide = new MenuAide();

    /**
     * @throws java.awt.HeadlessException
     */
    public MainClient() throws HeadlessException {
        this("");
    }

    /**
     * @param arg0
     * @throws java.awt.HeadlessException
     */
    public MainClient(String titre) throws HeadlessException {
        super(titre);
        setSize(300, 600);

        mBar.add(mDemarrer);
        mBar.add(mTaches);
        mBar.add(mAide);//**

        setJMenuBar(mBar);

        mDemarrer.setMenuListener(mml);
        mTaches.setMenuListener(mml);
        mAide.setMenuListener(mml);
        mp = new MainPanel();
        getContentPane().add(mp);

        cl = new Client();
        cl.addObserver(mp);
    }

    public static void main(String[] args) {
        new MainClient("Labo1 ver 2.0 ").setVisible(true);
    }

    class MainMenuListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            String action = evt.getActionCommand();
            if (action.equals("Se connecter")) {
                String a;
                while ((a = JOptionPane.showInputDialog(MainClient.this, "Votre alias pour cette connexion :", cl.getAlias())) != null && a.equals("")) {
                    JOptionPane.showMessageDialog(MainClient.this, "L'alias ne peut �tre vide");
                }

                cl.setAlias(a);

                if (!cl.connecter()) {
                    JOptionPane.showMessageDialog(MainClient.this, "Connexion impossible");
                    return;
                }

                setTitle(getTitle() + " - " + cl.getAlias());
                Object source = evt.getSource();
                if (source instanceof JMenuItem) {
                    ((JMenuItem) source).setText("Se deconnecter");
                    ((JMenuItem) source).setActionCommand("Se deconnecter");
                }
            } else if (action.equals("Se deconnecter")) {
                if (!(JOptionPane.showConfirmDialog(MainClient.this, "Se deconnecter ?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
                    return;
                }
                if (!cl.deconnecter()) {
                    JOptionPane.showMessageDialog(MainClient.this, "Deconnexion impossible");
                    return;
                }

                String titre = getTitle();
                int j = titre.indexOf("-");
                if (j > 0) {
                    setTitle(titre.substring(0, j - 1));
                }

                Object source = evt.getSource();
                if (source instanceof JMenuItem) {
                    ((JMenuItem) source).setText("Se connecter");
                    ((JMenuItem) source).setActionCommand("Se connecter");
                }
                //JOptionPane.showMessageDialog(MainClient.this,"vous �tes deconnect�s");
            } else if (action.equals("Configurer serveur")) {
                JPanel p1 = new JPanel(new GridLayout(2, 1));
                JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT)),
                        p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                JTextField t1 = new JTextField(cl.getAdrServeur(), 10);
                p2.add(new JLabel("Adresse du serveur : "));
                p2.add(t1);
                JTextField t2 = new JTextField(String.valueOf(cl.getPortServeur()), 10);
                p3.add(new JLabel("Port du serveur : "));
                p3.add(t2);
                p1.add(p2);
                p1.add(p3);
                int r = JOptionPane.showConfirmDialog(MainClient.this, p1, "Configurer serveur", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (r == JOptionPane.YES_OPTION) {
                    cl.setAdrServeur(t1.getText());
                    cl.setPortServeur(Integer.parseInt(t2.getText()));
                }
            } else if (action.equals("Quitter")) {
                if (!(JOptionPane.showConfirmDialog(MainClient.this, "Quitter ?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
                    return;
                }
                cl.deconnecter();
                System.exit(0);
            }
        }
    }
}

