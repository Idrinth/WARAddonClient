package de.idrinth.waraddonclient.gui;

import de.idrinth.waraddonclient.service.Config;
import javax.swing.JFrame;

abstract class BaseFrame extends JFrame {
    private Config config;

    protected BaseFrame(Config config) {
        this.config = config;
    }
    
    @Override
    public void setTitle(String title) {
        super.setTitle(title + " - Idrinth's WAR Addon Client " + config.getVersion());
    }
}
