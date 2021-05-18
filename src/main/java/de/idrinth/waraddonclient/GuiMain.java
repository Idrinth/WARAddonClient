package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.gui.FrameRestorer;
import de.idrinth.waraddonclient.gui.ThemeManager;
import de.idrinth.waraddonclient.gui.Window;
import de.idrinth.waraddonclient.model.GuiAddonList;
import de.idrinth.waraddonclient.service.Backup;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.FileSystem;
import de.idrinth.waraddonclient.service.FileWatcher;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.Restarter;
import de.idrinth.waraddonclient.service.Shedule;
import de.idrinth.waraddonclient.service.Version;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

public class GuiMain extends BaseMain {
   @Override
   protected void main(FileLogger logger, Config config, Request client, FileSystem file) throws FileSystem.FileSystemException, ParserConfigurationException, IOException {
       file.processPosition();
       Shedule schedule = new Shedule();
       Restarter restarter = new  Restarter(config);
       ThemeManager themes = new ThemeManager(logger, config, restarter);
       GuiAddonList addonList = new GuiAddonList(client, logger, new XmlParser(), config);
       FileWatcher watcher = new FileWatcher(addonList, logger, config);
       schedule.register(30, watcher);
       java.awt.EventQueue.invokeLater(() -> {
           Version version = new Version(client, logger);
           Window window = new Window(addonList, version, themes, logger, schedule, config, new Backup(config), restarter);
           new FrameRestorer(config).restore(window);
           window.setVisible(true);
       });
    }

    @Override
    public void error(Exception ex) {
        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
    }
}
