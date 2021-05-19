package de.idrinth.waraddonclient.service;

import java.util.ArrayList;
import java.util.List;

public class MultiLogger extends BaseLogger {
    private final List<BaseLogger> loggers = new ArrayList<>();

    public void add(BaseLogger logger) {
        for (BaseLogger loggr : loggers) {
            if (loggr.getClass().getName().equals(logger.getClass().getName())) {
                loggers.set(loggers.indexOf(loggr), logger);
                return;
            }
        }
        this.loggers.add(logger);
    }

    @Override
    protected void log(String message, String severity) {
        loggers.forEach(logger -> logger.log(message, severity));
    }
}
