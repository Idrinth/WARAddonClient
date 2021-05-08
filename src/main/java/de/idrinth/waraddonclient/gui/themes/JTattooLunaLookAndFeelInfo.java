package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.luna.LunaLookAndFeel;
import javax.swing.UIManager;

public class JTattooLunaLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooLunaLookAndFeelInfo() {
        super("JTattoo Luna", LunaLookAndFeel.class.getName());
    }
}
