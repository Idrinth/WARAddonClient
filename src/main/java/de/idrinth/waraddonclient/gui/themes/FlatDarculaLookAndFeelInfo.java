package de.idrinth.waraddonclient.gui.themes;

import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.UIManager;

public class FlatDarculaLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public FlatDarculaLookAndFeelInfo() {
        super("Flat Darcula", FlatDarculaLaf.class.getName());
    }
}
