package de.idrinth.waraddonclient.service;

import javax.swing.JOptionPane;

public class GuiLogger extends BaseLogger {

    @Override
    protected void log(String message, String severity) {
        if (!severity.equals(error)) {
            return;
        }
        JOptionPane.showMessageDialog(null, message);
    }
}
