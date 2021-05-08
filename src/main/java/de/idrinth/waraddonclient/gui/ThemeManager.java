package de.idrinth.waraddonclient.gui;

import de.idrinth.waraddonclient.Config;
import de.idrinth.waraddonclient.gui.themes.DarculaLookAndFeelInfo;
import de.idrinth.waraddonclient.Main;
import de.idrinth.waraddonclient.gui.themes.FlatDarculaLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.FlatDarkLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.FlatIntelliJLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.FlatLightLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.IdrinthLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooAcrylLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooAeroLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooAluminiumLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooBernsteinLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooFastLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooGraphiteLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooHiFiLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooLunaLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooMcWinLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooMintLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooNoireLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooSmartLookAndFeelInfo;
import de.idrinth.waraddonclient.gui.themes.JTattooTextureLookAndFeelInfo;
import java.awt.event.ActionEvent;
import java.io.IOException;
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
        UIManager.installLookAndFeel(new JTattooAcrylLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooAeroLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooAluminiumLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooBernsteinLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooFastLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooGraphiteLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooHiFiLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooLunaLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooMcWinLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooNoireLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooMintLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooSmartLookAndFeelInfo());
        UIManager.installLookAndFeel(new JTattooTextureLookAndFeelInfo());
        UIManager.installLookAndFeel(new FlatLightLookAndFeelInfo());
        UIManager.installLookAndFeel(new FlatIntelliJLookAndFeelInfo());
        UIManager.installLookAndFeel(new FlatDarkLookAndFeelInfo());
        UIManager.installLookAndFeel(new FlatDarculaLookAndFeelInfo());
    }

    public static void addTo(JMenu menu) {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            javax.swing.JCheckBoxMenuItem item = new javax.swing.JCheckBoxMenuItem();
            item.setText(info.getName());
            item.setSelected(Config.getTheme().equals(info.getName()));
            item.addActionListener((ActionEvent evt) -> {
                if (Config.getTheme().equals(info.getName())) {
                    return;
                }
                Config.setTheme(info.getName());
                try {
                    Main.restart();
                } catch (IOException ex) {
                    de.idrinth.factory.Logger.build().log("Failed to restart", de.idrinth.Logger.LEVEL_ERROR);
                }
            });
            menu.add(item);
        }
    }

    public static void init() {
        install();
        String preference = Config.getTheme();
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (preference.equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    de.idrinth.factory.Logger.build().log("Unable to load theme " + info.getName(), de.idrinth.Logger.LEVEL_ERROR);
                }
            }
        }
    }
}
