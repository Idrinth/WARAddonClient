package de.idrinth.waraddonclient.gui;

import java.awt.EventQueue;
import java.util.HashMap;

public class MainWindowMap extends HashMap<String, MainWindow> {
    public static final String ADDONS = "addons";
    public static final String SETTINGS = "settings";
    public static final String START = "start";
    public static final String BACKUPS = "backups";
    public void exchange(String from, String to) {
        EventQueue.invokeLater(() -> {
            MainWindow old = this.get(from);
            MainWindow nw = this.get(to);
            nw.setVisible(true);
            old.setVisible(false);
            nw.setLocation(old.getLocation());
            nw.setSize(old.getSize());
        });
    }
}
