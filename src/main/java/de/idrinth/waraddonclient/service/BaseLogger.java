package de.idrinth.waraddonclient.service;

public abstract class BaseLogger {

    protected abstract void log(String message, String severity);

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
