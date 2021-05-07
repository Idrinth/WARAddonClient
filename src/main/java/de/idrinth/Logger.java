package de.idrinth;

import java.io.File;
import de.idrinth.waraddonclient.factory.Interface;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;

public class Logger {

    public static final int LEVEL_INFO = 0;

    public static final int LEVEL_WARN = 1;

    public static final int LEVEL_ERROR = 2;

    private File output;

    /**
     * tries to initialize an output file
     */
    public Logger() {
        try {
            output = new File("idrinth.log");
            if (!output.exists()) {
                output.createNewFile();
            }
        } catch (Exception exception) {
            Interface.build().exitWithError(exception.getMessage());
        }
    }

    /**
     * Adds information to a give message
     *
     * @param message
     * @param severity
     * @return
     */
    private String buildMessage(String message, int severity) {
        String severityLabel;
        switch (severity) {
            case 0:
                severityLabel = "[Info] ";
                break;
            case 1:
                severityLabel = "[Warn] ";
                break;
            default:
                severityLabel = "[Error]";
                break;
        }
        return "[" + (new SimpleDateFormat("YYYY-MM-dd HH:mm:ss z")).format(Calendar.getInstance().getTime()) + "]" + severityLabel + " " + message + "\n";
    }

    /**
     * writes a message to file or if that files to system.out
     *
     * @param message
     * @param severity
     */
    public final void log(String message, int severity) {
        String formattedMessage = buildMessage(message, severity);
        try {
            FileUtils.writeStringToFile(output, formattedMessage, StandardCharsets.UTF_8, true);
        } catch (Exception exception) {
            Interface.build().exitWithError(formattedMessage + "\n" + exception.getMessage());
        }
    }

    /**
     * writes a message to file or if that files to system.out
     *
     * @param message
     * @param severity
     */
    public final void log(Throwable message, int severity) {
        log(message.getLocalizedMessage(),severity);
    }
}
