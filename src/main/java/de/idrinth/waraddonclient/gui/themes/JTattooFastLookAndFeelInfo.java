package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.fast.FastLookAndFeel;
import javax.swing.UIManager;

public class JTattooFastLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooFastLookAndFeelInfo() {
        super("JTattoo Fast", FastLookAndFeel.class.getName());
    }
}
