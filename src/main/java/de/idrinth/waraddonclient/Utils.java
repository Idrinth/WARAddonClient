package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.service.logger.BaseLogger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class Utils {

    private Utils() {
    }

    public static void emptyFolder(File folder) throws IOException {
        if (folder == null || !folder.exists()) {
            return;
        }
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                emptyFolder(file);
            }
            Files.deleteIfExists(file.toPath());
        }
    }

    public static void deleteFolder(File folder) throws IOException {
        if (folder == null || !folder.exists()) {
            return;
        }
        emptyFolder(folder);
        Files.deleteIfExists(folder.toPath());
    }
    public static void sleep(int duration, BaseLogger logger) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException exception) {
            logger.info(exception);
        }
    }
}
