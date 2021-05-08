package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.aero.AeroLookAndFeel;
import javax.swing.UIManager;

public class JTattooAeroLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooAeroLookAndFeelInfo() {
        super("JTattoo Aeoro", AeroLookAndFeel.class.getName());
    }
}
