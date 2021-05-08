package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.noire.NoireLookAndFeel;
import javax.swing.UIManager;

public class JTattooNoireLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooNoireLookAndFeelInfo() {
        super("JTattoo Noire", NoireLookAndFeel.class.getName());
    }
}
