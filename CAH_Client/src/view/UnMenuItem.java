/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.JMenuItem;
import java.awt.event.*;

/**
 * @author A. Toudeft
 */
public class UnMenuItem extends JMenuItem {

    /**
     *
     */
    public UnMenuItem(String s, char touche, ActionListener al, boolean b) {
        super(s, (int) touche);
        setActionCommand(s);
        setEnabled(b);
        addActionListener(al);
        // TODO Auto-generated constructor stub
    }
}
