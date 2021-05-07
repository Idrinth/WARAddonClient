/*
 * Copyright (C) 2016 Björn Büttner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.idrinth.waraddonclient.implementation.service;

import de.idrinth.waraddonclient.Config;
import java.io.File;

public class FileWatcher implements java.lang.Runnable {

    private java.nio.file.WatchService watcher;

    /**
     * initialises the filwatchin for the log-folder so uploads of changed data
     * may happen
     */
    @Override
    public void run() {
        try {
            File path = new File(Config.getWARPath() + "/logs");
            if (!path.exists()) {
                path.mkdirs();
            }
            watcher = path.toPath().getFileSystem().newWatchService();
            java.nio.file.WatchEvent.Kind[] modes = new java.nio.file.WatchEvent.Kind[2];
            modes[0] = java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
            modes[1] = java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
            path.toPath().register(
                    watcher,
                    modes,
                    com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE
            );
            handleEvents();
        } catch (InterruptedException | java.io.IOException exception) {
            de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.LEVEL_ERROR);
        }
    }

    /**
     * lets filesbe uploaded if so desired
     *
     * @throws InterruptedException
     */
    private void handleEvents() throws InterruptedException {
        while (true) {
            java.nio.file.WatchKey key = watcher.take();
            key.pollEvents().stream().filter((event) -> (isValidEvent(event))).map((event) -> new java.io.File(Config.getWARPath() + "/logs/" + event.context().toString())).filter((file) -> (isValidFile(file))).forEach((file) -> {
                de.idrinth.waraddonclient.factory.AddonList.build().getWatchedFiles().get(file.getName().toLowerCase()).setFileToProcess(file);
            });
            key.reset();
        }
    }

    /**
     * is this an event to process?
     *
     * @param event
     * @return
     */
    private boolean isValidEvent(java.nio.file.WatchEvent event) {
        return event.kind() == java.nio.file.StandardWatchEventKinds.ENTRY_CREATE || event.kind() == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
    }

    /**
     * is this a file to process?
     *
     * @param file
     * @return
     */
    private boolean isValidFile(java.io.File file) {
        return de.idrinth.waraddonclient.factory.AddonList.build().getWatchedFiles().containsKey(file.getName().toLowerCase());
    }
}
