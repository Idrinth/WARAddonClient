package de.idrinth.waraddonclient.factory;

import de.idrinth.waraddonclient.Config;
import java.io.File;
import java.io.IOException;

public final class Logger {

    private static de.idrinth.waraddonclient.service.FileLogger instance;

    /**
     * Prevent objects of this class
     */
    private Logger() {
        //not mean to be used
    }

    /**
     *
     * @return de.idrinth.Logger
     */
    public static synchronized de.idrinth.waraddonclient.service.FileLogger build() {
        if (instance == null) {
            try {
                instance = new de.idrinth.waraddonclient.service.FileLogger(new File(Config.getLogFile()));
            } catch (IOException ex) {
                //@todo
            }
        }
        return instance;
    }
}
