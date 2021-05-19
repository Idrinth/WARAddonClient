package de.idrinth.waraddonclient.service;

import javax.swing.JOptionPane;

public class GuiLogger extends BaseLogger {

    @Override
    protected void log(String message, String severity) {
        if (!severity.equals("Error")) {
            return;
        }
        JOptionPane.showMessageDialog(null, message);
    }
}
