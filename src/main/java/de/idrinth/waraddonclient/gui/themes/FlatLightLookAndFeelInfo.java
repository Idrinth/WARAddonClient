package de.idrinth.waraddonclient.gui.themes;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class FlatLightLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public FlatLightLookAndFeelInfo() {
        super("Flat Light", FlatLightLaf.class.getName());
    }
}
