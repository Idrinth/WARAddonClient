package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.gui.FrameRestorer;
import de.idrinth.waraddonclient.gui.ThemeManager;
import de.idrinth.waraddonclient.gui.Window;
import de.idrinth.waraddonclient.list.AddonList;
import de.idrinth.waraddonclient.model.TrustManager;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.FileSystem;
import de.idrinth.waraddonclient.service.FileWatcher;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.Version;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public final class Main {

    private static final String SUN_JAVA_COMMAND = "sun.java.command";

    private Main() {
        //not to be used
    }

    public static void main(String args[]) {
        try {
            FileLogger logger = new FileLogger(new File(Config.getLogFile()));
            logger.info("Starting");
            ThemeManager themes = new ThemeManager(logger);
            new FileSystem().processPosition();
            Request client = new Request(new TrustManager(logger), logger);
            AddonList addonList = new AddonList(client, logger);
            FileWatcher watcher = new FileWatcher(addonList, logger);
            new Thread(watcher).start();
            java.awt.EventQueue.invokeLater(() -> {
                Version version = new Version(client, logger);
                Window window = new Window(addonList, version, themes, logger);
                new FrameRestorer().restore(window);
                window.setVisible(true);
            });
        } catch (FileSystem.FileSystemException|IOException|CertificateException|KeyManagementException|KeyStoreException|NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            Runtime.getRuntime().exit(0);
        }
    }

    public static void restart() throws IOException {
        try {
            StringBuilder cmd = new StringBuilder("\"" + System.getProperty("java.home") + "/bin/java\" ");
            ManagementFactory.getRuntimeMXBean().getInputArguments().stream().filter(arg -> (!arg.contains("-agentlib"))).map(arg -> {
                cmd.append(arg);
                return arg;
            }).forEachOrdered(item -> cmd.append(" "));

            String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
            cmd.append("-jar " );
            cmd.append(new File(mainCommand[0]).getPath());
            for (int i = 1; i < mainCommand.length; i++) {
                cmd.append(" ");
                cmd.append(mainCommand[i]);
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        Runtime.getRuntime().exec(cmd.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.exit(0);
        } catch (Exception e) {
            throw new IOException("Error while trying to restart the application", e);
        }
    }
}
