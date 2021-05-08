package de.idrinth.waraddonclient.gui.themes;

import com.jtattoo.plaf.bernstein.BernsteinLookAndFeel;
import javax.swing.UIManager;

public class JTattooBernsteinLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public JTattooBernsteinLookAndFeelInfo() {
        super("JTattoo Bernstein", BernsteinLookAndFeel.class.getName());
    }
}
