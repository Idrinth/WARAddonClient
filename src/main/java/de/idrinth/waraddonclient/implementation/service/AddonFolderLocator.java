package de.idrinth.waraddonclient.implementation.service;

import de.idrinth.waraddonclient.Config;
import java.io.File;

public final class AddonFolderLocator {

    /**
     * not to be callsed
     */
    private AddonFolderLocator() {
        //should never be constructed
    }

    public static java.io.File find(String name) {
        String[] path = new String[3];
        path[0] = "interface";
        path[1] = "addons";
        path[2] = name;
        File folder = new File(Config.getWARPath());
        for (int counter = 0; counter < 3; counter++) {
            folder = findMatch(folder, path[counter]);
            if (folder == null || !folder.exists()) {
                return null;
            }
        }
        return folder;
    }

    private static java.io.File findMatch(File folder, String search) {
        if (!folder.exists()) {
            return null;
        }
        for (java.io.File innerFolder : folder.listFiles()) {
            if (innerFolder.getName().equalsIgnoreCase(search)) {
                return innerFolder;
            }
        }
        return null;
    }

}
