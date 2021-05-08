package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.mint.MintLookAndFeel;
import javax.swing.UIManager;

public class JTattooMintLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooMintLookAndFeelInfo() {
        super("JTattoo Mint", MintLookAndFeel.class.getName());
    }
}
