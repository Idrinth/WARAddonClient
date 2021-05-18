package de.idrinth.waraddonclient.service;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileSystem {

    private final Config config;

    public FileSystem(Config config) {
        this.config = config;
    }

    public void checkPosition() throws FileSystemException {
        if (new java.io.File(config.getWARPath() + "/WAR.exe").exists()) {
            return;
        }
        throw new FileSystemException("No WAR.exe found in the specified folder, please try setting it again");
    }

    public void processPosition() throws FileSystemException {
        try {
            checkPosition();
            return;
        } catch (FileSystemException ex) {
            //ignore, the following code will handle it
        }
        JOptionPane.showMessageDialog(null, "No WAR.exe found, please select it");
        JFileChooser j = new javax.swing.JFileChooser();
        int r = j.showOpenDialog(null);

        // if the user selects a file
        if (r == JFileChooser.APPROVE_OPTION) {
            config.setWARPath(j.getSelectedFile().getParent());
        }
        checkPosition();
    }

    public class FileSystemException extends Exception {
        public FileSystemException(String message) {
            super(message);
        }
    }
}
