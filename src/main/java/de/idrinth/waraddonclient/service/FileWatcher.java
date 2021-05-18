package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.model.GuiAddonList;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class FileWatcher implements java.lang.Runnable {

    private final GuiAddonList addonList;

    private final WatchService watcher;

    private final FileLogger logger;
    
    private final Config config;

    public FileWatcher(GuiAddonList addonList, FileLogger logger, Config config) throws IOException {
        this.addonList = addonList;
        this.logger = logger;
        this.config = config;
        File path = new File(config.getLogsFolder());
        if (!path.exists()) {
            path.mkdirs();
        }
        watcher = path.toPath().getFileSystem().newWatchService();
        WatchEvent.Kind[] modes = new WatchEvent.Kind[2];
        modes[0] = StandardWatchEventKinds.ENTRY_CREATE;
        modes[1] = StandardWatchEventKinds.ENTRY_MODIFY;
        path.toPath().register(watcher, modes);
    }

    @Override
    public void run() {
        try {
            WatchKey key = watcher.take();
            key.pollEvents().stream().filter(event -> (isValidEvent(event))).map(event -> new File(config.getLogsFolder() + event.context().toString())).filter(file -> (isValidFile(file))).forEach(file -> addonList.getWatchedFiles().get(file.getName().toLowerCase()).setFileToProcess(file));
            key.reset();
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
    }

    /**
     * is this an event to process?
     *
     * @param event
     * @return
     */
    private boolean isValidEvent(WatchEvent event) {
        return event.kind() == StandardWatchEventKinds.ENTRY_CREATE || event.kind() == StandardWatchEventKinds.ENTRY_MODIFY;
    }

    /**
     * is this a file to process?
     *
     * @param file
     * @return
     */
    private boolean isValidFile(java.io.File file) {
        return addonList.getWatchedFiles().containsKey(file.getName().toLowerCase());
    }
}
