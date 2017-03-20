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
public class MenuAide extends UnMenu {

    JMenuItem miAPropos;

    /**
     *
     */
    public MenuAide() {
        setText("Aide");
        setActionCommand("Aide");
        setMnemonic((int) 'A');
        miAPropos = new UnMenuItem("A propos...", 'A', mil, true);

        miAPropos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));

        add(miAPropos);
    }
}
