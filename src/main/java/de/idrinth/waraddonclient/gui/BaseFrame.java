package de.idrinth.waraddonclient.gui;

import de.idrinth.waraddonclient.service.Config;
import java.awt.Toolkit;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JFrame;

abstract class BaseFrame extends JFrame {
    private final AtomicReference<Config> config = new AtomicReference<>();

    protected BaseFrame(Config config) {
        this.config.set(config);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
    }
    
    @Override
    public void setTitle(String title) {
        super.setTitle(title + " - Idrinth's WAR Addon Client " + config.get().getVersion());
    }
}
