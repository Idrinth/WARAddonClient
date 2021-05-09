package de.idrinth.waraddonclient.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;

final public class FileLogger {
    private final File output;

    public FileLogger(File output) throws IOException {
        if (!output.exists()) {
            output.createNewFile();
        }
        this.output = output;
    }

    public void log(String message, String severity) {
        try {
            FileUtils.writeStringToFile(
                output,
                "[" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")).format(Calendar.getInstance().getTime()) + "][" + severity + "] " + message + "\n",
                StandardCharsets.UTF_8,
                true
            );
        } catch (Exception exception) {
            //ignore
        }
    }

    public void info(Throwable message) {
        info(message.getLocalizedMessage());
    }

    public void warn(Throwable message) {
        warn(message.getLocalizedMessage());
    }

    public void error(Throwable message) {
        error(message.getLocalizedMessage());
    }

    public void info(String message) {
        log(message, "INFO");
    }

    public void warn(String message) {
        log(message, "WARN");
    }

    public void error(String message) {
        log(message, "ERROR");
    }
}
