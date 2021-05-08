package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import javax.swing.UIManager;

public class JTattooAcrylLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooAcrylLookAndFeelInfo() {
        super("JTattoo Acryl", AcrylLookAndFeel.class.getName());
    }
}
