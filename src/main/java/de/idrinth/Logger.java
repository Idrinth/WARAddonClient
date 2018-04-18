/*
 * Copyright (C) 2016 Björn Büttner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.idrinth;

import java.io.File;
import de.idrinth.waraddonclient.factory.Interface;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;

public class Logger {

    public final static int levelInfo = 0;

    public final static int levelWarn = 1;

    public final static int levelError = 2;

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
