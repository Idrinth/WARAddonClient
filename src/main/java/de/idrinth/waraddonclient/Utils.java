package de.idrinth.waraddonclient;

import java.io.File;

public class Utils {

    private Utils() {
    }

    /**
     * empties a folder
     *
     * @param folder
     */
    public static void emptyFolder(File folder) {
        if (folder == null || !folder.exists()) {
            return;
        }
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                emptyFolder(file);
            }
            file.delete();
        }
    }
}
