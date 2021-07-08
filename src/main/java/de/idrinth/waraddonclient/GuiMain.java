package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.gui.FrameRestorer;
import de.idrinth.waraddonclient.gui.MainWindowMap;
import de.idrinth.waraddonclient.gui.Progress;
import de.idrinth.waraddonclient.gui.Settings;
import de.idrinth.waraddonclient.gui.ThemeManager;
import de.idrinth.waraddonclient.gui.Addons;
import de.idrinth.waraddonclient.gui.Backups;
import de.idrinth.waraddonclient.gui.MainWindow;
import de.idrinth.waraddonclient.gui.Start;
import de.idrinth.waraddonclient.model.GuiAddonList;
import de.idrinth.waraddonclient.service.Backup;
import de.idrinth.waraddonclient.service.FileSystem;
import de.idrinth.waraddonclient.service.FileWatcher;
import de.idrinth.waraddonclient.service.logger.GuiLogger;
import de.idrinth.waraddonclient.service.logger.MultiLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.Restarter;
import de.idrinth.waraddonclient.service.Shedule;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

public class GuiMain extends BaseMain {

    public GuiMain() {
        add(new GuiLogger());
    }
    
   @Override
   protected void main(MultiLogger logger, Config config, Request client, FileSystem file) throws FileSystem.FileSystemException, ParserConfigurationException, IOException {
       file.processPosition();
       Shedule schedule = new Shedule();
       Restarter restarter = new  Restarter(config);
       ThemeManager themes = new ThemeManager();
       themes.install();
       themes.applyTheme(logger, config);
       GuiAddonList addonList = new GuiAddonList(client, logger, new XmlParser(), config);
       FileWatcher watcher = new FileWatcher(addonList, logger, config);
       schedule.register(30, watcher);
       java.awt.EventQueue.invokeLater(() -> {
           Progress progress = new Progress(config);
           FrameRestorer restorer = new FrameRestorer(config);
           MainWindowMap map = new MainWindowMap();
           map.put(MainWindowMap.ADDONS, new Addons(map, addonList, logger, schedule, config, progress));
           map.put(MainWindowMap.SETTINGS, new Settings(map, config));
           map.put(MainWindowMap.START, new Start(map, logger, config, restarter, client));
           map.put(MainWindowMap.BACKUPS, new Backups(map, logger, config, new Backup(config), progress, schedule));
           for (MainWindow window : map.values()) {
               restorer.restore(window);
           }
           restorer.restore(progress);
           map.get(MainWindowMap.START).setVisible(true);
       });
    }
}
