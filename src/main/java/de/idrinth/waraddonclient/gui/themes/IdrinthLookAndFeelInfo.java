package de.idrinth.waraddonclient.gui.themes;

import javax.swing.UIManager;

public class IdrinthLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public IdrinthLookAndFeelInfo() {
        super("Idrinth", IdrinthLookAndFeel.class.getName());
    }
}
