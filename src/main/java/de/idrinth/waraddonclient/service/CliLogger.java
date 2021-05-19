package de.idrinth.waraddonclient.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CliLogger extends BaseLogger {
    private final boolean verbose;

    public CliLogger(boolean isVerbose) {
        verbose = isVerbose;
    }

    @Override
    protected void log(String message, String severity) {
        String formatted = "[" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")).format(Calendar.getInstance().getTime()) + "][" + severity + "] " + message;
        if (severity.equals(info)) {
            if (verbose) {
                System.out.println(formatted);
            }
            return;
        }
        System.err.println(formatted);
    }
}
