package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import javax.swing.UIManager;

public class JTattooHiFiLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooHiFiLookAndFeelInfo() {
        super("JTattoo HiFi", HiFiLookAndFeel.class.getName());
    }
}
