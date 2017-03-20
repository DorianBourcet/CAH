/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author A. Toudeft
 */
public class UnMenu extends JMenu {

    ActionListener menuListener = null;
    MenuItemListener mil = new MenuItemListener();

    /**
     *
     */
    public UnMenu() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public UnMenu(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @param arg1
     */
    public UnMenu(String arg0, boolean arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public UnMenu(Action arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public void setMenuListener(ActionListener al) {
        menuListener = al;
    }

    public void setEnabled(String nomItem, boolean b) {
        Component[] c = getMenuComponents();
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof JMenuItem) {
                JMenuItem jmi = (JMenuItem) c[i];
                if (jmi.getText().equals(nomItem)) {
                    jmi.setEnabled(b);
                }
            }
        }
    }

    class MenuItemListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            if (menuListener != null) {
                menuListener.actionPerformed(evt);
            }
        }
    }
}
