package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.texture.TextureLookAndFeel;
import javax.swing.UIManager;

public class JTattooTextureLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooTextureLookAndFeelInfo() {
        super("JTattoo Texture", TextureLookAndFeel.class.getName());
    }
}
