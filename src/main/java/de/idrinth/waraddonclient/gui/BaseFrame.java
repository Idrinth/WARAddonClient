package de.idrinth.waraddonclient.gui;

import de.idrinth.waraddonclient.service.Config;
import java.awt.Toolkit;
import javax.swing.JFrame;

abstract class BaseFrame extends JFrame {
    private Config config;

    protected BaseFrame(Config config) {
        this.config = config;
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
    }
    
    @Override
    public void setTitle(String title) {
        super.setTitle(title + " - Idrinth's WAR Addon Client " + config.getVersion());
    }
}
