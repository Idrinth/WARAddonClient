package de.idrinth.waraddonclient.service;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.lang.SystemUtils;

public class FileSystem
{
    private final Config config;

    public FileSystem(Config config) {
        this.config = config;
    }

    public void checkPosition() throws FileSystemException {
        if (new java.io.File(config.getWARPath() + "/WAR.exe").exists()) {
            if (config.getWARPath().equals(".")) {
                config.setWARPath(new File(".").getPath());
            }
            return;
        }
        if (checkDefaultPositions()) {
            return;
        }
        throw new FileSystemException("No WAR.exe found in the specified folder, please try setting it again");
    }

    private String[] getDefaultPositions()
    {
        if (SystemUtils.IS_OS_WINDOWS) {
            return new String[]{
                "C:\\Return of Reckoning",
                "C:\\Warhammer Online",
                "C:\\ReturnOfReckoning-March2020",
                "C:\\Games\\Return of Reckoning",
                "C:\\Games\\Warhammer Online",
                "C:\\Games\\ReturnOfReckoning-March2020",
                "C:\\Program Files (x86)\\Return of Reckoning",
                "C:\\Program Files (x86)\\Warhammer Online",
                "C:\\Program Files (x86)\\Warhammer Online - Age of Reckoning",
            };
        }
        if (SystemUtils.IS_OS_LINUX) {
            String home = System.getProperty("user.home");
            return new String[]{
                home + "/Games/warhammer-online-return-of-reckoning/prefix/drive_c/Program Files (x86)/Return of Reckoning",
                home + "/Games/warhammer-online-return-of-reckoning/drive_c/Program Files (x86)/Return of Reckoning",
            };
        }
        return new String[0];
    }

    private boolean checkDefaultPositions()
    {
        for (String path : getDefaultPositions()) {
            if (new File(path + "/WAR.exe").exists()) {
                config.setWARPath(path);
                return true;
            }
        }
        return false;
    }

    public void processPosition() throws FileSystemException {
        try {
            checkPosition();
            return;
        } catch (FileSystemException ex) {
            //ignore, the following code will handle it
        }
        JOptionPane.showMessageDialog(null, "No WAR.exe found, please select it");
        JFileChooser j = new JFileChooser();
        int r = j.showOpenDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {
            config.setWARPath(j.getSelectedFile().getParent());
        }
        checkPosition();
    }

    public static class FileSystemException extends Exception {
        public FileSystemException(String message) {
            super(message);
        }
    }
}
