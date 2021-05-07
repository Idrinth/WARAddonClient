package de.idrinth.waraddonclient.gui.themes;

import javax.swing.UIManager;
import com.bulenkov.darcula.DarculaLaf;

public class DarculaLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public DarculaLookAndFeelInfo() {
        super("Darcula", DarculaLaf.class.getName());
    }
}
