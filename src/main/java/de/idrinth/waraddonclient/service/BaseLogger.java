package de.idrinth.waraddonclient.service;

public abstract class BaseLogger {
    protected static final String info = "INFO";

    protected static final String error = "ERROR";

    protected static final String warning = "WARN";

    protected abstract void log(String message, String severity);

    public void info(Throwable message) {
        info(message.getLocalizedMessage());
    }

    public void info(String message) {
        log(message, info);
    }

    public void warn(Throwable message) {
        warn(message.getLocalizedMessage());
    }

    public void warn(String message) {
        log(message, warning);
    }

    public void error(Throwable message) {
        error(message.getLocalizedMessage());
    }

    public void error(String message) {
        log(message, error);
    }
}
