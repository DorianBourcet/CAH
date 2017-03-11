/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 * @author A. Toudeft
 */
public class MenuTaches extends UnMenu {

    JMenuItem miConfigurerServeur;

    /**
     *
     */
    public MenuTaches() {
        setText("T�ches");
        setActionCommand("T�ches");
        setMnemonic((int) 'T');
        miConfigurerServeur = new UnMenuItem("Configurer serveur", 'S', mil, true);

        miConfigurerServeur.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));

        add(miConfigurerServeur);
    }
}

