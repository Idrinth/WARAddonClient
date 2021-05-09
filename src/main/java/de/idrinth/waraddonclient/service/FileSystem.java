package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.Config;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileSystem {
    public void processPosition() throws FileSystemException {
        if (new java.io.File(Config.getWARPath() + "/WAR.exe").exists()) {
            return;
        }
        JOptionPane.showMessageDialog(null, "No WAR.exe found, please select it");
        JFileChooser j = new javax.swing.JFileChooser();
        int r = j.showOpenDialog(null);

        // if the user selects a file
        if (r == JFileChooser.APPROVE_OPTION) {
            Config.setWARPath(j.getSelectedFile().getParent());
        }
        if (new java.io.File(Config.getWARPath() + "/WAR.exe").exists()) {
            return;
        }
        throw new FileSystemException("Unable to find WAR.exe");
    }
    public class FileSystemException extends Exception {

        public FileSystemException() {
        }

        public FileSystemException(String message) {
            super(message);
        }

        public FileSystemException(String message, Throwable cause) {
            super(message, cause);
        }

        public FileSystemException(Throwable cause) {
            super(cause);
        }

        public FileSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
        
    }
}
