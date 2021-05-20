package de.idrinth.waraddonclient.service.logger;

import javax.swing.JOptionPane;

public class GuiLogger extends BaseLogger {

    @Override
    protected void log(String message, String severity) {
        if (!severity.equals(LEVEL_ERROR)) {
            return;
        }
        JOptionPane.showMessageDialog(null, message);
    }
}
