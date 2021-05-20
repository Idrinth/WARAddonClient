package de.idrinth.waraddonclient.service.logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;

public final class FileLogger extends BaseLogger {
    private final File output;

    public FileLogger(File output) throws IOException {
        if (!output.exists()) {
            output.createNewFile();
        }
        this.output = output;
    }

    @Override
    protected void log(String message, String severity) {
        if (severity.equals(LEVEL_INFO)) {
            return;
        }
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
}
