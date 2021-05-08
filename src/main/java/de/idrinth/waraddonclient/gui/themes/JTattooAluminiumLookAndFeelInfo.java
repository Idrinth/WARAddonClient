package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import javax.swing.UIManager;

public class JTattooAluminiumLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooAluminiumLookAndFeelInfo() {
        super("JTattoo Aluminium", AluminiumLookAndFeel.class.getName());
    }
}
