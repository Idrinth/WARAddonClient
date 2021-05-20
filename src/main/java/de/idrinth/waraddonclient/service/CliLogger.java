package de.idrinth.waraddonclient.service;

public class CliLogger extends BaseLogger {
    private final boolean verbose;

    public CliLogger(boolean isVerbose) {
        verbose = isVerbose;
    }

    @Override
    protected void log(String message, String severity) {
        String formatted = "[" + severity + "] " + message;
        if (severity.equals(info)) {
            if (verbose) {
                System.out.println(formatted);
            }
            return;
        }
        System.err.println(formatted);
    }
}
