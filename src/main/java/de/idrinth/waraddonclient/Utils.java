package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.service.FileLogger;
import java.io.File;

public class Utils {

    private Utils() {
    }

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
    public static void sleep(int duration, FileLogger logger) {
        try {
            Thread.sleep(duration);
        } catch (java.lang.InterruptedException exception) {
            logger.info(exception);
        }
    }
}
