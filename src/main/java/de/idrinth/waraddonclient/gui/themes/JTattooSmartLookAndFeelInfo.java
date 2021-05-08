package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.smart.SmartLookAndFeel;
import javax.swing.UIManager;

public class JTattooSmartLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooSmartLookAndFeelInfo() {
        super("JTattoo Smart", SmartLookAndFeel.class.getName());
    }
}
