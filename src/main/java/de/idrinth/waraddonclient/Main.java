package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.gui.FrameRestorer;
import de.idrinth.waraddonclient.gui.ThemeManager;
import de.idrinth.waraddonclient.gui.Window;
import de.idrinth.waraddonclient.model.AddonList;
import de.idrinth.waraddonclient.model.TrustManager;
import de.idrinth.waraddonclient.service.Backup;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.FileSystem;
import de.idrinth.waraddonclient.service.FileWatcher;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.Shedule;
import de.idrinth.waraddonclient.service.Version;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;

public final class Main {

    private Main() {
        //not to be used
    }

    public static void main(String[] args) {
        try {
            Config config = new Config();
            FileLogger logger = new FileLogger(config.getLogFile());
            ThemeManager themes = new ThemeManager(logger, config);
            new FileSystem(config).processPosition();
            Shedule schedule = new Shedule();
            Request client = new Request(new TrustManager(logger), logger, config);
            AddonList addonList = new AddonList(client, logger, new XmlParser(), config);
            FileWatcher watcher = new FileWatcher(addonList, logger, config);
            schedule.register(30, watcher);
            if (args.length > 0) {
                for (String arg : args) {
                    if (arg.trim().contains("--updateonly")) {
                        addonList.updateAll();
                        Runtime.getRuntime().exit(0);
                    }
                }
            }
            else {
                java.awt.EventQueue.invokeLater(() -> {
                    Version version = new Version(client, logger);
                    Window window = new Window(addonList, version, themes, logger, schedule, config, new Backup(config));
                    new FrameRestorer(config).restore(window);
                    window.setVisible(true);
                });
            }

        } catch (ParserConfigurationException |FileSystem.FileSystemException|IOException|CertificateException|KeyManagementException|KeyStoreException|NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            Runtime.getRuntime().exit(0);
        }
    }

    public static void restart() throws IOException {
        try {
            File jar = new File("WARAddonClient.jar");
            File exe = new File("WARAddonClient.exe");
            if (exe.exists()) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec(exe.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                System.exit(0);
            } else if (jar.exists()) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec("java -jar "+jar.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                System.exit(0);
            }
            throw new IOException("Executable not found, did you replace it?");
        } catch (IOException e) {
            throw new IOException("Error while trying to restart the application", e);
        }
    }
}
