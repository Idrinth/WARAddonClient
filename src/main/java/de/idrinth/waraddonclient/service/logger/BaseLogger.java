package de.idrinth.waraddonclient.service.logger;

public abstract class BaseLogger {
    protected static final String LEVEL_INFO = "INFO";

    protected static final String LEVEL_ERROR = "ERROR";

    protected static final String LEVEL_WARNING = "WARN";

    protected abstract void log(String message, String severity);

    public void info(Throwable message) {
        info(message.getLocalizedMessage());
    }

    public void info(String message) {
        log(message, LEVEL_INFO);
    }

    public void warn(Throwable message) {
        warn(message.getLocalizedMessage());
    }

    public void warn(String message) {
        log(message, LEVEL_WARNING);
    }

    public void error(Throwable message) {
        error(message.getLocalizedMessage());
    }

    public void error(String message) {
        log(message, LEVEL_ERROR);
    }
}
