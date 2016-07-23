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

public class Logger {

    public final static int levelInfo = 0;
    public final static int levelWarn = 1;
    public final static int levelError = 2;
    protected static String[] severityLabel;
    protected java.io.File output = null;

    public Logger() {
        severityLabel = new String[3];
        severityLabel[0] = "Info ";
        severityLabel[1] = "Warn ";
        severityLabel[2] = "Error";
        try {
            java.io.File output = new java.io.File("idrinth.log");
            if (!output.exists()) {
                output.createNewFile();
            }
        } catch (java.lang.Exception exception) {
            this.log(exception.getMessage(), levelError);
        }
    }

    protected String buildMessage(String message, int severity) {
        return "[" + (new java.text.SimpleDateFormat("YYYY-mm-dd hh:mm:ss")).format(java.util.Calendar.getInstance().getTime()) + "][" + severityLabel[severity] + "] " + message + "\n";
    }

    public void log(String message, int severity) {
        message = buildMessage(message, severity);
        if (output == null) {
            System.out.println(message);
            return;
        }
        try {
            org.apache.commons.io.FileUtils.writeStringToFile(output, message, null, true);
        } catch (Exception exception) {
            System.out.println(message);
            System.out.println(exception.getMessage());
        }
    }
}
