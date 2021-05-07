package de.idrinth.waraddonclient.gui;

import de.idrinth.waraddonclient.gui.themes.DarculaLookAndFeelInfo;
import de.idrinth.waraddonclient.Main;
import de.idrinth.waraddonclient.gui.themes.IdrinthLookAndFeelInfo;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.swing.JMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class ThemeManager {

    private ThemeManager() {
        //not to be used
    }

    private static void install() {
        UIManager.installLookAndFeel(new DarculaLookAndFeelInfo());
        UIManager.installLookAndFeel(new IdrinthLookAndFeelInfo());
    }

    public static void addTo(JMenu menu, Preferences prefs) {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            javax.swing.JCheckBoxMenuItem item = new javax.swing.JCheckBoxMenuItem();
            item.setText(info.getName());
            item.setSelected(prefs.get("theme", "Nimbus").equals(info.getName()));
            item.addActionListener((ActionEvent evt) -> {
                if (prefs.get("theme", "Nimbus").equals(info.getName())) {
                    return;
                }
                prefs.put("theme", info.getName());
                try {
                    Main.restart();
                } catch (IOException ex) {
                    de.idrinth.factory.Logger.build().log("Failed to restart", de.idrinth.Logger.levelError);
                }
            });
            menu.add(item);
        }
    }

    public static void init(Preferences prefs) {
        install();
        String preference = prefs.get("theme", "Nimbus");
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (preference.equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    de.idrinth.factory.Logger.build().log("Unable to load theme " + info.getName(), de.idrinth.Logger.levelError);
                }
            }
        }
    }
}
